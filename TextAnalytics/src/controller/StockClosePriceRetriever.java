package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import orm.Company;
import orm.SessionManager;

public class StockClosePriceRetriever {
	private final Map<Integer, Double> dayIndexStockPriceMap;
	
	private final Query query;
	
	protected static final String stockPriceQueryStr = "select adjustedClose from StockData " +
			"where company = (:company) and dayIndex >= (:dayIndex) order by dayIndex asc";
	
	public StockClosePriceRetriever(final Company company) {
		dayIndexStockPriceMap = new HashMap<Integer, Double>();
		query = SessionManager.createQuery(stockPriceQueryStr);
		query.setParameter("company", company);
		query.setFirstResult(0);
		query.setMaxResults(1);
	}
	
	public Double getStockAdjustedClosePrice(Integer dayIndex) {
		Double result = dayIndexStockPriceMap.get(dayIndex);
		if (null == result) {
			query.setParameter("dayIndex", dayIndex);

			@SuppressWarnings("rawtypes")
			List objList = query.list();
			result = objList.size() > 0 ? (Double)objList.get(0) : null;
			if (result != null) {
				dayIndexStockPriceMap.put(dayIndex, result);
			} else {
				System.err.println("Did not find stock price for day index " + dayIndex);
			}
		}
		
		return result;
	}
}
