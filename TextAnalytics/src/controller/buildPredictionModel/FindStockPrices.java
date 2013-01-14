package controller.buildPredictionModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.Company;
import orm.SessionManager;
import orm.StockData;

public class FindStockPrices {
	private static final String dayIndexParam = "dayIndex";
	private static final String companyParam = "company";
	
	private static final String queryStr = "from StockData where dayIndex >= (:"
			+ dayIndexParam + ") and company=(:" + companyParam + ")order by dayIndex asc";	
	
	private final List<StockData> stockDataList;
	
	FindStockPrices(List<StockData> stockDataList) {
		this.stockDataList = stockDataList;
	}
	
	public FindStockPrices(int minDayIndex, Company company) {
		Query query = SessionManager.createQuery(queryStr);
		query.setParameter(companyParam, company);
		query.setParameter(dayIndexParam, minDayIndex);

		System.out.println("FindNextStockPrices constructor looking up stock data");
		stockDataList = Utilities.convertGenericList(query.list());
		System.out.println("FindNextStockPrices constructor stock data lookup done");
	}
	
	/**
	 * 
	 * @param dayIndexList sorted list of day indexes to use to lookup StockData
	 * @return
	 */
	public Map<Integer, StockData> findNext(List<Integer> dayIndexList) {
		Iterator<Integer> dayIndexIter = dayIndexList.iterator();
		
		final Map<Integer, StockData> result = new HashMap<>();
		
		Iterator<StockData> stockDataIter = stockDataList.iterator();
		
		while (dayIndexIter.hasNext() && stockDataIter.hasNext()) {
			final int targetDayIndex = dayIndexIter.next();
			
			StockData stockData = null;
			while (stockDataIter.hasNext() && 
					(stockData = stockDataIter.next()).getDayIndex() < targetDayIndex) { }
			
			if (stockData != null) {
				result.put(targetDayIndex, stockData);
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param dayIndexList list of dayIndexes that the weighted average of closing price
	 * @param weights must contain an odd number doubles to be used in creating a weighted average
	 * @return
	 */
	public Map<Integer, Double> findWeightedAverage(List<Integer> dayIndexList, double[] weights) {
		final Map<Integer, Double> result = new HashMap<>();
		
		final int numDaysEachSide = weights.length / 2;
		
		Iterator<Integer> dayIndexIter = dayIndexList.iterator();
		
		ListIterator<StockData> stockDataIter = stockDataList.listIterator();
		
		while (dayIndexIter.hasNext() && stockDataIter.hasNext()) {
			final int targetDayIndex = dayIndexIter.next();
			
			StockData stockData = null;
			while (stockDataIter.hasNext() && 
					(stockData = stockDataIter.next()).getDayIndex() < targetDayIndex) { }
			
			if (stockData.getDayIndex() >= targetDayIndex) {
				final int currentIndex = stockDataIter.previousIndex();
				
				final int startIndex = currentIndex - numDaysEachSide >= 0 ? currentIndex - numDaysEachSide : 0;
				final int startWeightIndex = numDaysEachSide - (currentIndex - startIndex);
				
				final int endIndex = currentIndex + numDaysEachSide < stockDataList.size() 
						? currentIndex + numDaysEachSide : stockDataList.size() - 1;

				double sum = 0.0;
				int weightIndex = startWeightIndex;
				double weightsSum = 0.0;
				ListIterator<StockData> aveIter = stockDataList.listIterator(startIndex);
				for (int i = startIndex; i <= endIndex; i++) {
					StockData aveStockData = aveIter.next();
					sum += weights[weightIndex] * aveStockData.getAdjustedClose();
					
					weightsSum += weights[weightIndex];
					
					weightIndex++;
				}
				
				final double ave = sum / weightsSum;
				
				result.put(targetDayIndex, ave);
			} else {
				System.err.println("FindStockPrices findWeightedAverageNearDayIndexes requested dayIndex " 
						+ targetDayIndex + " is after last day of StockData " + stockData.getDayIndex());
			}
		}

		return result;
	}

}
