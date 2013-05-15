package research.regression.volatility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.stockPrices.FindStockPrices;

import orm.Company;
import orm.ScoringModel;
import orm.Eigenvalue;
import orm.EigenvectorValue;
import orm.SessionManager;
import orm.StockData;
import research.correlation.SumData;
import research.regression.GetPcData;


/**
 * NOT VALIDATED for scoringModel changes
 * 
 * @author dlahr
 *
 */
public class GetVolatilityData {
	
	private static final int scoringModelId = 1; //CAT
	private static final int companyId = 1;
	
	private static final int dayOffset = 40;

	public static void main(String[] args) {
		System.out.println("GetVolatilityData start");
		
		ScoringModel scoringModel = GetPcData.lookupScoringModel(scoringModelId);
		System.out.println("Company: " + scoringModel.getId());
		
		Company company = scoringModel.getCompanyById(companyId);
		if (null == company) {
			System.err.println("could not find company with ID " + companyId + " associated with scoring model");
			return;
		} else {
			System.out.println("for company " + company);
		}
		
		System.out.println("lookup eigenvector values");
		List<EigenvectorValue> evvList = GetPcData.lookupEigenvectorValues(scoringModel);
		
		System.out.println("get article day indexes");
		List<Integer> articleDayIndexList = GetPcData.calculateUniqueArticleDays(evvList);
		
		System.out.println("aggregate eigenvector values by article day index");
		Map<Eigenvalue, Map<Integer, SumData>> eigDayIndexSumDataMap = GetPcData.calculateEigMap(evvList);
		
		List<Eigenvalue> eigList = new ArrayList<>(eigDayIndexSumDataMap.keySet());
		Collections.sort(eigList, new Comparator<Eigenvalue>() {
			@Override
			public int compare(Eigenvalue o1, Eigenvalue o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		
		System.out.println("calculate stock price fraction change for offset");
		Map<Integer, Double> articleDayIndexStockVolatilityMap = calcStockVolatility(articleDayIndexList, dayOffset, company);

		List<Integer> newDayIndexList = new ArrayList<>(articleDayIndexStockVolatilityMap.keySet());
		Collections.sort(newDayIndexList);
		for (Integer dayIndex : newDayIndexList) {
			System.out.println(dayIndex + "," + articleDayIndexStockVolatilityMap.get(dayIndex));
		}
		
		SessionManager.closeAll();
		System.out.println("GetVolatilityData done");
	}
	
	static Map<Integer, Double> calcStockVolatility(List<Integer> articleDayIndexList, int dayOffset, 
			Company company) {
		
		final int minDayIndex = Collections.min(articleDayIndexList);
		FindStockPrices findNextStockPrices = new FindStockPrices(minDayIndex, company);
		
		Map<Integer, StockData> articleDayStockDataMap = findNextStockPrices.findNext(articleDayIndexList);
		
		Map<Integer, Double> result = new HashMap<>();
		for (Integer articleDayIndex : articleDayIndexList) {
			final StockData initialData = articleDayStockDataMap.get(articleDayIndex);
			if (initialData != null) {
				List<Integer> nextDayList = new ArrayList<>(dayOffset);
				for (int i = 1; i <= dayOffset; i++) {
					nextDayList.add(articleDayIndex + i);
				}
				
				Map<Integer, StockData> stockDataMap = findNextStockPrices.findNext(nextDayList);
				stockDataMap.put(articleDayIndex, initialData);
				
				int count = 0;
				double volatility = 0.0;
				for (int i = articleDayIndex; i < (articleDayIndex + dayOffset); i++) {
					StockData curData = stockDataMap.get(i);
					if (curData != null) {
						StockData nextData = stockDataMap.get(i+1);
						if (nextData != null) {
							count++;
							volatility += Math.abs((nextData.getAdjustedClose()/curData.getAdjustedClose()) - 1.0);
						}
					}
				}
				if (count > 0) {
					volatility = volatility / count;
					result.put(articleDayIndex, volatility);
				}
			}
		}
		
		return result;
	}
}
