package controller;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import orm.Company;
import orm.SessionManager;
import orm.UtilitiesForTesting;

public class StockClosePriceRetrieverTest  {

	private static final Integer[] dayIndexesWithValuesArray = {1, 5, 6, 7};
	private static final Integer[] dayIndexesWithoutValuesArray = {8, 13};

	@Test
	public void test() {
		SessionManager.setUseForTest(true);
		List<Company> companyList = UtilitiesForTesting.getFirst10Entities("Company");
		
		StockClosePriceRetriever retriever = new StockClosePriceRetriever(companyList.get(0));
	
		for (Integer dayIndexWithValue : dayIndexesWithValuesArray) {
			Double value = retriever.getStockAdjustedClosePrice(dayIndexWithValue);
			assertNotNull(value);
			assertTrue(value >= dayIndexWithValue);
		}
		
		for (Integer dayIndexWithoutValue : dayIndexesWithoutValuesArray) {
			assertNull(retriever.getStockAdjustedClosePrice(dayIndexWithoutValue));
		}
		
	}

}
