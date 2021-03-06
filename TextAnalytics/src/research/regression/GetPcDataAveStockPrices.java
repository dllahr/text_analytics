package research.regression;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orm.Company;
import orm.ScoringModel;
import orm.Eigenvalue;
import orm.EigenvectorValue;
import orm.SessionManager;
import research.correlation.SumData;
import controller.stockPrices.SmoothedStockPrices;


/**
 * @author dlahr
 */
public class GetPcDataAveStockPrices {
	private static final String eigPrefix = "eig";
	
	private static final int dayOffset = 40;
	
	private static final double[] weightsArray = {0.5, 1.0, 0.5};

	public static void main(String[] args) throws IOException {
		final int scoringModelId = Integer.valueOf(args[0]);
		final int companyId = Integer.valueOf(args[1]);

		final Date startDate = new Date();
		System.out.println("start regression GetPcData " + startDate);
		
		ScoringModel scoringModel = ScoringModel.getScoringModel(scoringModelId);
		System.out.println("for scoringModel " + scoringModel);
		
		Company company = Company.findById(companyId);
		if (null == company) {
			System.err.println("could not find company with ID " + companyId + " associated with scoring model");
			return;
		} else {
			System.out.println("for company " + company);
		}
		
		String outputFilename = "regressionPcSmoothStockData_" + scoringModel.getId() + ".csv";
		File outputFile = new File(outputFilename);
		if (outputFile.exists()) {
			System.err.println("output file already exists, exiting:  " + outputFile.getAbsolutePath());
			return;
		}
		
		System.out.println("lookup eigenvector values " + new Date());
		List<EigenvectorValue> evvList = EigenvectorValue.getEigenvectorValuesOrderByArticleDayIndexAndEigId(scoringModel);
		
		System.out.println("get article day indexes " + new Date());
		List<Integer> articleDayIndexList = RegressionUtils.calculateUniqueArticleDays(evvList);
		
		System.out.println("aggregate eigenvector values by article day index " + new Date());
		Map<Eigenvalue, Map<Integer, SumData>> eigDayIndexSumDataMap = RegressionUtils.calculateEigMap(evvList);
		
		List<Eigenvalue> eigList = new ArrayList<>(eigDayIndexSumDataMap.keySet());
		Collections.sort(eigList, new Comparator<Eigenvalue>() {
			@Override
			public int compare(Eigenvalue o1, Eigenvalue o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		
		System.out.println("calculate smoothed stock price fraction change for offset " + new Date());
		Map<Integer, Double> articleDayIndexStockChangeMap = calcStockFractionChange(articleDayIndexList, dayOffset, 
				company, weightsArray);
		
		System.out.println("write to file " + new Date());
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		writer.write("dayIndex,");
		for (Eigenvalue eig : eigList) {
			writer.write(eigPrefix + eig.getId() + ",");
		}
		writer.write("frac_change");
		writer.newLine();
		
		for (Integer dayIndex : articleDayIndexList) {
			final Double stockChange = articleDayIndexStockChangeMap.get(dayIndex);
			if (stockChange != null) {
				writer.write(dayIndex + ",");

				for (Eigenvalue eig : eigList) {
					Map<Integer, SumData> dayIndexSumDataMap = eigDayIndexSumDataMap.get(eig);
					SumData sumData = dayIndexSumDataMap.get(dayIndex);
					final double value = sumData != null ? sumData.value / sumData.count : 0.0;

					writer.write(value + ",");
				}

				writer.write(String.valueOf(stockChange));
				writer.newLine();
			}
		}
		
		writer.close();
		SessionManager.closeAll();
		Date endDate = new Date();
		final double durationMinutes = ((double)(endDate.getTime() - startDate.getTime())) / 60000.0;
		
		printGlmExpression(eigList);
		
		System.out.println("end regression GetPcData " + endDate + " duration[min]:  " + durationMinutes);
	}
	
	
	static Map<Integer, Double> calcStockFractionChange(List<Integer> articleDayIndexList, int dayOffset, 
			Company company, double[] weightsArray) {

		//need to back the number of days on the side of of the average + an additional week to be safe
		final int minDayIndex = Collections.min(articleDayIndexList) - (7 + weightsArray.length/2);
		SmoothedStockPrices findSmoothedStockPrices = new SmoothedStockPrices(minDayIndex, company, weightsArray);
		
		List<Integer> articleDayIndexOffsetList = new ArrayList<>(articleDayIndexList.size());
		for (Integer dayIndex : articleDayIndexList) {
			articleDayIndexOffsetList.add(dayIndex + dayOffset);
		}
		
		Map<Integer, Double> result = new HashMap<>();
		for (Integer articleDayIndex : articleDayIndexList) {
			final Double initialAve = findSmoothedStockPrices.getSmoothedPriceClosestAfterDayIndex(articleDayIndex);
			if (initialAve != null) {
				final Double finalAve = findSmoothedStockPrices.getSmoothedPriceClosestAfterDayIndex(articleDayIndex + dayOffset);
				if (finalAve != null) {
					result.put(articleDayIndex, finalAve / initialAve);
				}
			}
		}
		
		return result;
	}
	
	static void printGlmExpression(List<Eigenvalue> eigList) {
		for (Eigenvalue eig : eigList) {
			System.out.print(eigPrefix + eig.getId() + " + ");
		}
		System.out.println();
	}
}
