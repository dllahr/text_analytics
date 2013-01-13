package controller.buildPredictionModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
	public Map<Integer, StockData> find(List<Integer> dayIndexList) {
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

}
