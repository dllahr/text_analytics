package controller.buildPredictionModel;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import controller.util.Utilities;

import orm.Company;
import orm.SessionManager;
import orm.StockPriceChange;

public class CalculateStockStatisticsTest {

	@Test
	public void test() {
		SessionManager.setUseForTest(true);
		List<Company> companyList = Utilities.convertGenericList(SessionManager.createQuery("from Company where id=1").list());
		Company company = companyList.get(0);

		List<Integer> dayIndexList = new ArrayList<>(1);
		dayIndexList.add(5);
		dayIndexList.add(7);

		CalculateStockStatistics calcStockStats = new CalculateStockStatistics(true, dayIndexList.get(0), company);
		
		List<StockPriceChange> result = calcStockStats.doCalc(dayIndexList);
		
//		for (StockPriceChange spc : result) {
//			System.out.println(spc.getDayOffset() + " " + spc.getAverage() + " " + spc.getFwhm());
//		}
		
		double change = (15.4/15.15) - 1.0;
		change += ((15.0/14.14) - 1.0);
		change /= 2.0;
		
		assertEquals(change, result.get(0).getAverage(), 1e-10);
	}

}
