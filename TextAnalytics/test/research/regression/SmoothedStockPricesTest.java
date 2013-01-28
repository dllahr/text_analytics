package research.regression;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.junit.Test;

import orm.Company;
import orm.SessionManager;
import orm.StockData;

public class SmoothedStockPricesTest {
	
	@Test
	public void testIntegration() {
		SessionManager.setUseForTest(false);
		Query query = SessionManager.createQuery("from Company where stockSymbol='CAT'");
		Company company = (Company)(query.list().get(0));
		double[] weights = {0.5, 1.0, 0.5};
		final int minDayIndex = 5877;
		SmoothedStockPrices findSmoothedStockPrices = new SmoothedStockPrices(minDayIndex, company, weights);
		
		for (int i = 1; i <= 14; i++) {
			final int dayIndex = i + minDayIndex;
			final double value = findSmoothedStockPrices.getSmoothedPriceClosestAfterDayIndex(dayIndex);
			System.out.println(dayIndex + " " + value);
		}
		
		//compared to values calculated in spreadsheet test_smoothing.xlsx - they match
	}

	@Test
	public void testBuildDayIndexStockDataMap() {
		Map<Integer, Double> rawData = new HashMap<>();
		rawData.put(0, 1.0);
		rawData.put(1, 2.0);
		rawData.put(4, 3.0);
		rawData.put(5, 5.0);
		rawData.put(9, 7.0);
		rawData.put(10, 11.0);
		
		Map<Integer, Double> fullMap = SmoothedStockPrices.buildDayIndexStockDataMap(rawData);
		
		for (int i = 0; i <= 10; i++) {
			assertTrue(fullMap.containsKey(i));
			
			final double expected;
			if (rawData.containsKey(i)) {
				expected = rawData.get(i);
			} else {
				if (i < 4) {
					expected = 3.0;
				} else {
					expected = 7.0;
				}
			}
			final double found = fullMap.get(i);
			
			System.out.println(i + " " + expected + " " + found);
			assertEquals(expected, found, 1e-5);
		}
	}

	@Test
	public void testCalculateSmoothedPrices() {
		double[] weights = {0.25, 1.0, 0.75};
		int[] dayIndexes = {0, 1, 2};
		double[] prices = {2.0, 3.0, 5.0};
		
		basicTest(weights, dayIndexes, prices);
		
		basicTest(weights, dayIndexes, new double[]{-2.0, -3.0, -5.0});
		
		basicTest(weights, dayIndexes, new double[]{13.0, 17.0, 19.0});
		
		basicTest(new double[]{1.0, 1.0, 1.0}, dayIndexes, prices);
		
		basicTest(new double[]{2.0, 1.0, 3.0}, dayIndexes, prices);
		
		basicTest(weights, new int[]{0,2,4}, prices);
		basicTest(weights, new int[]{0,3,7}, prices);
		basicTest(weights, new int[]{11,17,23}, prices);
		basicTest(weights, new int[]{-23,-17, -11}, prices);
		
		basicTest(new double[]{1.0, 2.0, 3.0, 5.0, 7.0}, new int[]{0, 1, 2, 3, 4}, new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
	}
	
	private static void basicTest(double[] weights, int[] dayIndexes, double[] prices) {
		final double weightsSum = sum(weights);
		
		final int targetDayInd = dayIndexes[dayIndexes.length/2];
		
		final double smoothed = multiplyArray(weights, prices) / weightsSum;

		List<StockData> list = new LinkedList<>();
		for (int i = 0; i < dayIndexes.length; i++) {
			StockData sd = new StockData();
			sd.setDayIndex(dayIndexes[i]);
			sd.setAdjustedClose(prices[i]);

			list.add(sd);
		}

		Map<Integer, Double> result = SmoothedStockPrices.calculateSmoothedPrices(list, weights);
		assertEquals(1, result.size());
		
		final int resultDayIndex = result.keySet().iterator().next();
		assertEquals(targetDayInd, resultDayIndex);
		
		final double resultValue = result.get(targetDayInd);
		assertEquals(smoothed, resultValue, 1e-5);
		
		System.out.println(resultDayIndex + " " + resultValue);
	}
	
	private static double multiplyArray(double[] arr1, double[] arr2) {
		double result = 0.0;
		
		for (int i = 0; i < arr1.length; i++) {
			result += arr1[i] * arr2[i];
		}
		
		return result;
	}
	
	private static double sum(double[] arr) {
		double result = 0.0;
		
		for (double val : arr) {
			result += val;
		}
		
		return result;
	}
}
