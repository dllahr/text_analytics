package research.regression;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;

import orm.Company;
import orm.ScoringModel;
import orm.Eigenvalue;
import orm.EigenvectorValue;
import orm.SessionManager;
import orm.StockData;
import research.correlation.SumData;
import controller.buildPredictionModel.FindStockPrices;
import controller.util.Utilities;

/**
 * NOT VALIDATED for scoringModel changes
 * 
 * @author dlahr
 *
 */
public class GetPcData {
	private static final int scoringModelId = 6;
	
	private static final int companyId = 6;
	
	private static final int dayOffset = 40;

	public static void main(String[] args) throws IOException {
		System.out.println("start regression GetPcData");
		
		ScoringModel scoringModel = lookupScoringModel(scoringModelId);
		System.out.println("for scoringModel " + scoringModel.getId());
		
		Company company = scoringModel.getCompanyById(companyId);
		if (null == company) {
			System.err.println("could not find company with ID " + companyId + " associated with scoring model");
			return;
		} else {
			System.out.println("for company " + company);
		}
		
		String outputFilename = "regressionPcStockData_" + scoringModel.getId() + ".csv";
		File outputFile = new File(outputFilename);
		if (outputFile.exists()) {
			System.err.println("output file already exists, exiting:  " + outputFile.getAbsolutePath());
			return;
		}
		
		System.out.println("lookup eigenvector values");
		List<EigenvectorValue> evvList = lookupEigenvectorValues(scoringModel);
		
		System.out.println("get article day indexes");
		List<Integer> articleDayIndexList = calculateUniqueArticleDays(evvList);
		
		System.out.println("aggregate eigenvector values by article day index");
		Map<Eigenvalue, Map<Integer, SumData>> eigDayIndexSumDataMap = calculateEigMap(evvList);
		
		List<Eigenvalue> eigList = new ArrayList<>(eigDayIndexSumDataMap.keySet());
		Collections.sort(eigList, new Comparator<Eigenvalue>() {
			@Override
			public int compare(Eigenvalue o1, Eigenvalue o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		
		System.out.println("calculate stock price fraction change for offset");
		Map<Integer, Double> articleDayIndexStockChangeMap = calcStockFractionChange(articleDayIndexList, dayOffset, company);
		
		System.out.println("write to file");
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		writer.write("dayIndex,");
		for (Eigenvalue eig : eigList) {
			writer.write(eig.getId() + ",");
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
		System.out.println("end regression GetPcData");
	}
	
	static Map<Integer, Double> calcStockFractionChange(List<Integer> articleDayIndexList, int dayOffset, 
			Company company) {

		final int minDayIndex = Collections.min(articleDayIndexList);
		FindStockPrices findNextStockPrices = new FindStockPrices(minDayIndex, company);
		
		Map<Integer, StockData> articleDayStockDataMap = findNextStockPrices.findNext(articleDayIndexList);
		
		List<Integer> articleDayIndexOffsetList = new ArrayList<>(articleDayIndexList.size());
		for (Integer dayIndex : articleDayIndexList) {
			articleDayIndexOffsetList.add(dayIndex + dayOffset);
		}
		
		Map<Integer, StockData> articleDayIndexOffsetStockDataMap = findNextStockPrices.findNext(articleDayIndexOffsetList);
		
		Map<Integer, Double> result = new HashMap<>();
		for (Integer articleDayIndex : articleDayIndexList) {
			final StockData initialData = articleDayStockDataMap.get(articleDayIndex);
			if (initialData != null) {
				final StockData finalData = articleDayIndexOffsetStockDataMap.get(articleDayIndex + dayOffset);
				if (finalData != null) {
					result.put(articleDayIndex, finalData.getAdjustedClose() / initialData.getAdjustedClose());
				}
			}
		}
		
		return result;
	}
	
	public static Map<Eigenvalue, Map<Integer, SumData>> calculateEigMap(List<EigenvectorValue> evvList) {
		Map<Eigenvalue, Map<Integer, SumData>> result = new HashMap<Eigenvalue, Map<Integer, SumData>>();
		
		for (EigenvectorValue evv : evvList) {
			Map<Integer, SumData> dayValueMap = result.get(evv.getEigenvalue());
			if (null == dayValueMap) {
				dayValueMap = new HashMap<>();
				result.put(evv.getEigenvalue(), dayValueMap);
			}
			
			SumData sumData = dayValueMap.get(evv.getArticle().getDayIndex());
			if (null == sumData) {
				sumData = new SumData();
				dayValueMap.put(evv.getArticle().getDayIndex(), sumData);
			}
			
			sumData.value += evv.getValue();
			sumData.count++;
		}
		
		return result;
	}

	public static List<Integer> calculateUniqueArticleDays(List<EigenvectorValue> evvList) {
		Set<Integer> articleDaySet = new HashSet<>();
		Set<Integer> nullDayIndexArticleIdSet = new HashSet<>();
		
		for (EigenvectorValue evv : evvList) {
			final Integer dayIndex = evv.getArticle().getDayIndex();
			
			if (dayIndex != null) {
				articleDaySet.add(evv.getArticle().getDayIndex());
			} else {
				nullDayIndexArticleIdSet.add(evv.getArticle().getId());
			}
		}
		System.out.println("\tarticles with null dayIndex:  " + nullDayIndexArticleIdSet.size());
		
		List<Integer> articleDayIndexList = new ArrayList<>(articleDaySet);
		Collections.sort(articleDayIndexList);
		return articleDayIndexList;
	}

	public static List<EigenvectorValue> lookupEigenvectorValues(ScoringModel company) {
		final String companyParam = "companyParam";
		Query query = SessionManager.createQuery("from EigenvectorValue where eigenvalue.company = :" + companyParam 
				+ " order by article.dayIndex asc, eigenvalue.id");
		query.setParameter(companyParam, company);
		
		return Utilities.convertGenericList(query.list());
	}

	public static ScoringModel lookupScoringModel(int scoringModelId) {
		final String scoringModelIdParam = "scoringModelId";	
		Query query = SessionManager.createQuery("from ScoringModel where id = :" + scoringModelIdParam);
		query.setParameter(scoringModelIdParam, scoringModelId);
		
		return (ScoringModel)query.uniqueResult();
	}
}
