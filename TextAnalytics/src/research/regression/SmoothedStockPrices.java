package research.regression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import orm.Company;
import orm.StockData;
import controller.buildPredictionModel.FindStockPrices;

public class SmoothedStockPrices {
	
	private final Map<Integer, Double> dayIndexSmoothedMap;


	/**
	 * weights must have odd number of entries
	 * @param minDayIndex
	 * @param company
	 * @param weights
	 */
	public SmoothedStockPrices(int minDayIndex, Company company, double[] weights) {
		final List<StockData> stockDataList = FindStockPrices.lookupStockData(minDayIndex, company);
		
		Map<Integer, Double> rawDayIndexSmoothedPriceMap = calculateSmoothedPrices(stockDataList, weights);
		
		dayIndexSmoothedMap = buildDayIndexStockDataMap(rawDayIndexSmoothedPriceMap);
	}
	
	
	public double getSmoothedPriceClosestAfterDayIndex(int dayIndex) {
		Double result = dayIndexSmoothedMap.get(dayIndex);
		if (result != null) {
			return result;
		} else {
			throw new RuntimeException("FindSmoothedStockPrices getSmoothedPriceClosestAfterDayIndex provided " +
					"dayIndex is not found in map - is it before the minDayIndex provided to the constructor or " +
					"after the last available dayIndex in the database?");
		}
	}


	
	static Map<Integer, Double> calculateSmoothedPrices(List<StockData> stockDataList, double[] weights) {
		double weightsSum = 0.0;
		for (double weight : weights) {
			weightsSum += weight;
		}
		
		final int centralIndex = weights.length/2;

		final List<StockData> window = new ArrayList<>(weights.length);
		
		Iterator<StockData> leadingIterator = stockDataList.iterator();
		
		//initialize priceList so that it looks like e.g.:
		//[null p0 p1 p2 p3 p4]
		window.add(null);
		for (int i = 0; i < (weights.length-1); i++) {
			window.add(leadingIterator.next());
		}
		
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		
		while (leadingIterator.hasNext()) {
			//slide the window over by 1 entry
			window.remove(0);
			window.add(leadingIterator.next());
			
			//calculate weighted average
			double sum = 0.0;
			int windowIndex = 0;
			for (StockData current : window) {
				sum += weights[windowIndex] * current.getAdjustedClose();
				
				windowIndex++;
			}
			final double weightedAve = sum / weightsSum;
			
			final int targetDayIndex = window.get(centralIndex).getDayIndex();
			
			result.put(targetDayIndex, weightedAve);
		}
		
		return result;
	}
	
	static Map<Integer, Double> buildDayIndexStockDataMap(Map<Integer, Double> rawSmoothedMap) {
		Map<Integer, Double> result = new HashMap<>();
		
		List<Integer> dayIndexList = new ArrayList<>(rawSmoothedMap.keySet());
		Collections.sort(dayIndexList);
		
		int prevDayIndex = dayIndexList.get(0);		
		
		for (Integer dayIndex : dayIndexList) {
			double price = rawSmoothedMap.get(dayIndex);
			
			while (prevDayIndex <= dayIndex) {
				result.put(prevDayIndex, price);
				prevDayIndex++;
			}
		}
		
		return result;
	}
	
	
}
