package controller.buildPredictionModel;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import controller.buildPredictionModel.FindStockPrices;
import controller.util.Utilities;

import orm.Company;
import orm.SessionManager;
import orm.StockData;

public class FindStockPricesTest {

	private static final int dayIndex = 9004;
	private static final int dayIndexResult = 9006;
	
	@Test
	public void test() {
		List<Company> companyList = Utilities.convertGenericList(SessionManager.createQuery("from Company where id=2").list());
		assertTrue(companyList.size() > 0);
		
		List<Integer> dayIndexList = new LinkedList<>();
		dayIndexList.add(dayIndex);
		
		FindStockPrices findNextStockPrices = new FindStockPrices(dayIndex, companyList.get(0));
		
		Map<Integer, StockData> result = findNextStockPrices.findNext(dayIndexList);
		assertTrue(result.keySet().size() > 0);
		assertTrue(result.containsKey(dayIndex));
		assertTrue(result.get(dayIndex).getDayIndex() == dayIndexResult);
	}

}
