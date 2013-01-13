package research.regression;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;

import orm.Company;
import orm.Eigenvalue;
import orm.EigenvectorValue;
import orm.SessionManager;
import orm.StockData;
import research.correlation.SumData;
import controller.buildPredictionModel.FindStockPrices;
import controller.util.Utilities;

public class GetPcData2 {
	private static final int companyId = 1;
	
	private static final int dayOffset = 40;
	
	private static final double fractionThreshold = 0.1;

	private static final int filenameIndex = 9;
	
	public static void main(String[] args) throws IOException {
		System.out.println("start regression GetPcData");
		
		Company company = lookupCompany(companyId);
		System.out.println("for company " + company.getStockSymbol());
		
		System.out.println("lookup eigenvector values");
		List<EigenvectorValue> evvList = lookupEigenvectorValues(company);
		
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
		BufferedWriter writer = new BufferedWriter(new FileWriter("regressionPcStockData_" + company.getStockSymbol() 
				+ "_" + filenameIndex +  ".csv"));
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
		
		Map<Integer, StockData> articleDayStockDataMap = findNextStockPrices.find(articleDayIndexList);
		
		List<Integer> articleDayIndexOffsetList = new ArrayList<>(articleDayIndexList.size());
		for (Integer dayIndex : articleDayIndexList) {
			articleDayIndexOffsetList.add(dayIndex + dayOffset);
		}
		
		Map<Integer, StockData> articleDayIndexOffsetStockDataMap = findNextStockPrices.find(articleDayIndexOffsetList);
		
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
	
	private static Map<Eigenvalue, Map<Integer, SumData>> calculateEigMap(List<EigenvectorValue> evvList) {
		int maxSize = 0;
		Map<Integer, Set<Eigenvalue>> map = new HashMap<>();
		for (EigenvectorValue evv : evvList) {
			Set<Eigenvalue> evSet = map.get(evv.getArticle().getDayIndex());
			if (null == evSet) {
				evSet = new HashSet<>();
				map.put(evv.getArticle().getDayIndex(), evSet);
			}
			evSet.add(evv.getEigenvalue());
			if (evSet.size() > maxSize) {
				maxSize = evSet.size();
			}
		}
		Set<Integer> dayIndexSet = new HashSet<>(map.keySet());
		Iterator<Integer> dayIndexIter = dayIndexSet.iterator();
		while (dayIndexIter.hasNext()) {
			Integer dayIndex = dayIndexIter.next();
			
			if (map.get(dayIndex).size() < (0.75*maxSize)) {
				dayIndexIter.remove();
			}
		}
		
		Map<Eigenvalue, Map<Integer, SumData>> result = new HashMap<Eigenvalue, Map<Integer, SumData>>();
		
		for (EigenvectorValue evv : evvList) {
			if (dayIndexSet.contains(evv.getArticle().getDayIndex())) {
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
		}
		
		return result;
	}

	private static List<Integer> calculateUniqueArticleDays(List<EigenvectorValue> evvList) {
		Set<Integer> articleDaySet = new HashSet<>();
		
		int nullDayIndexCount = 0;
		for (EigenvectorValue evv : evvList) {
			final Integer dayIndex = evv.getArticle().getDayIndex();
			
			if (dayIndex != null) {
				articleDaySet.add(evv.getArticle().getDayIndex());
			} else {
				nullDayIndexCount++;
			}
		}
		System.out.println("\tarticles with null dayIndex:  " + nullDayIndexCount);
		
		List<Integer> articleDayIndexList = new ArrayList<>(articleDaySet);
		Collections.sort(articleDayIndexList);
		return articleDayIndexList;
	}

	private static List<EigenvectorValue> lookupEigenvectorValues(Company company) {
		final String companyParam = "companyParam";
		Query query = SessionManager.createQuery("from EigenvectorValue where eigenvalue.company = :" + companyParam 
				+ " order by article.dayIndex asc, eigenvalue.id");
		query.setParameter(companyParam, company);
		
		Map<Eigenvalue, Thresholds> eigThresholdsMap = lookupThresholds(company);
		
		List<EigenvectorValue> result = Utilities.convertGenericList(query.list());
		
		Iterator<EigenvectorValue> iter = result.iterator();
		while (iter.hasNext()) {
			EigenvectorValue evv = iter.next();
			
			Thresholds thresholds = eigThresholdsMap.get(evv.getEigenvalue());
			
			if (evv.getValue() > thresholds.lowerThreshold && evv.getValue() < thresholds.upperThreshold) { //
				iter.remove();
			}
		}
		
		return result;
	}
	
	private static Map<Eigenvalue, Thresholds> lookupThresholds(Company company) {
		final String companyParam = "companyParam";
		Query query = SessionManager.createQuery("select count(*) from EigenvectorValue where eigenvalue.company=:" + companyParam
				+ " group by eigenvalue");
		query.setParameter(companyParam, company);
		query.setMaxResults(1);
		
		double count = (double)(Long)(query.list().get(0));
//		System.out.println(count);
		
		int lowerThresholdInd = (int)(Math.round(count*fractionThreshold));
		int upperThresholdInd = (int)(Math.round(count*(1.0 - fractionThreshold)));
		System.out.println("threshold indexes:  " + lowerThresholdInd + " " + upperThresholdInd);
		
		
		query = SessionManager.createQuery("from Eigenvalue where company=:" + companyParam);
		query.setParameter(companyParam, company);
		List<Eigenvalue> eigvalList = Utilities.convertGenericList(query.list());
		
		Map<Eigenvalue, Thresholds> resultMap = new HashMap<>();
		
		final String eigvalParam = "eigval";
		query = SessionManager.createQuery("select value from EigenvectorValue where eigenvalue = :" + eigvalParam + " order by value asc");
		query.setMaxResults(1);
		
		for (Eigenvalue eigval : eigvalList) {
			query.setParameter(eigvalParam, eigval);

			Thresholds thresholds = new Thresholds();
			
			query.setFirstResult(lowerThresholdInd-1);
			@SuppressWarnings("rawtypes")
			List thresholdList = query.list();
//			System.out.println("size:  " + result.size());
			if (thresholdList.size() > 0) {
				thresholds.lowerThreshold = (double)thresholdList.get(0);
			
				query.setFirstResult(upperThresholdInd-1);
				thresholdList = query.list();
				thresholds.upperThreshold = (double)thresholdList.get(0);
				
//				System.out.println(eigval.getId() + " " + thresholds.lowerThreshold + " "+ thresholds.upperThreshold);
				resultMap.put(eigval, thresholds);
			}
		}
		
		return resultMap;
	}

	private static Company lookupCompany(int companyId) {
		final String companyIdParam = "companyId";	
		Query query = SessionManager.createQuery("from Company where id = :" + companyIdParam);
		query.setParameter(companyIdParam, companyId);
		
		return (Company)query.uniqueResult();
	}
	
	
}
