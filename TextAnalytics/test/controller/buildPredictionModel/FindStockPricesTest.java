package controller.buildPredictionModel;

import static org.junit.Assert.*;

import java.util.ArrayList;
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

	private static final double eps = 1e-5;
	
	private static final int dayIndex = 9004;
	private static final int dayIndexResult = 9006;
	
	@Test
	public void testFindNext() {
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

	@Test
	public void testFindWeightedAverage() {
		int[] days = {0,1,2,3,4};
		final double[] prices = {2.0, 3.0, 5.0};
		
		final double[] weights = {0.33, 1.0, 0.7};
		
		double expected = calcWeightedAve(prices, weights);
		
		double[] allPrices = null;
		double actual = Double.NaN;
		//test by moving target day around without cutting off edges
		for (int targetDay = 1; targetDay  <= 3; targetDay++) {
			allPrices = buildAllPrices(days.length, targetDay, prices);
			
			actual = runWeightedTest(days, targetDay, allPrices, weights);
			
			System.out.println(expected + " " + actual);
			
			assertEquals(expected, actual, (eps)*expected);
		}
		
		//calculate on left edge
		//prices:   2.0, 3.0, 5.0, 0.0, 0.0
		//weights:  1.0, 0.7, ---, ---, ---
		allPrices = buildAllPrices(days.length, 1, prices);
		actual = runWeightedTest(days, 0, allPrices, weights);
		expected = ((prices[0]*weights[1]) + (prices[1]*weights[2])) / (weights[1] + weights[2]);
		System.out.println(expected + " " + actual);
		assertEquals(expected, actual, (eps)*expected);
		
		//calculate on right edge
		//prices:   0.0, 0.0, 2.0, 3.0, 5.0
		//weights:  ---, ---, ---, 0.33, 1.0
		allPrices = buildAllPrices(days.length, 3, prices);
		actual = runWeightedTest(days, 4, allPrices, weights);
		expected = ((weights[0]*prices[1]) + (weights[1]*prices[2])) / (weights[0] + weights[1]);
		System.out.println(expected + " " + actual);
		assertEquals(expected, actual, (eps)*expected);
		
		
		//test where target day does not exactly match recorded days
		days = new int[]{0, 2, 4, 6, 8};
		expected = calcWeightedAve(prices, weights);
		
		//test by moving target day around without cutting off edges
		for (int targetDay = 1; targetDay  <= 5; targetDay += 2) {
			allPrices = buildAllPrices(days.length, (targetDay+1)/2, prices);
			
			actual = runWeightedTest(days, targetDay, allPrices, weights);
			
			System.out.println(expected + " " + actual);
			
			assertEquals(expected, actual, (eps)*expected);
		}
		
		//calculate on left edge
		//prices:   2.0, 3.0, 5.0, 0.0, 0.0
		//weights:  1.0, 0.7, ---, ---, ---
		allPrices = buildAllPrices(days.length, 1, prices);
		actual = runWeightedTest(days, -1, allPrices, weights);
		expected = ((prices[0]*weights[1]) + (prices[1]*weights[2])) / (weights[1] + weights[2]);
		System.out.println(expected + " " + actual);
		assertEquals(expected, actual, (eps)*expected);
		
		//calculate on right edge
		//prices:   0.0, 0.0, 2.0, 3.0, 5.0
		//weights:  ---, ---, ---, 0.33, 1.0
		allPrices = buildAllPrices(days.length, 3, prices);
		actual = runWeightedTest(days, 7, allPrices, weights);
		expected = ((weights[0]*prices[1]) + (weights[1]*prices[2])) / (weights[0] + weights[1]);
		System.out.println(expected + " " + actual);
		assertEquals(expected, actual, (eps)*expected);

	}


	private static double runWeightedTest(int[] days, int targetDay, double[] prices, double[] weights) {
		assertEquals(days.length, prices.length);
				
		final List<StockData> stockDataList = buildStockDataList(days, prices);
		assertEquals(days.length, stockDataList.size());
		
		final FindStockPrices findStockPrices = new FindStockPrices(stockDataList);
		
		final List<Integer> dayIndexList = new ArrayList<>(1);
		dayIndexList.add(targetDay);
		
		Map<Integer, Double> resultMap = findStockPrices.findWeightedAverage(dayIndexList, weights);
		return resultMap.get(targetDay);
	}
	
	private static double[] buildAllPrices(int numDays, int targetDayIndex, double[] prices) {
		final double[] allPrices = new double[numDays];
		for (int i = 0; i < allPrices.length; i++) {
			allPrices[i] = 0.0;
		}
		
		int priceIndex = 0;
		final int halfNumWeights = prices.length / 2;
		for (int i = targetDayIndex - halfNumWeights; i <= targetDayIndex + halfNumWeights; i++) {
			allPrices[i] = prices[priceIndex];
			priceIndex++;
		}
		
		
		return allPrices;
	}
	
	private static double calcWeightedAve(double[] prices, double[] weights)  {
		assertEquals(prices.length, weights.length);
		
		double weightsSum = 0.0;
		double sum = 0.0;
		for (int i = 0; i < prices.length; i++) {
			weightsSum += weights[i];
			sum += prices[i]*weights[i];
		}
		
		return sum / weightsSum;
	}
	
	private static List<StockData> buildStockDataList(int[] days, double[] prices) {
		assertEquals(days.length, prices.length);

		List<StockData> result = new ArrayList<>(prices.length);
		
		for (int i = 0; i < days.length; i++) {
			StockData stockData = new StockData();
			stockData.setDayIndex(days[i]);
			stockData.setAdjustedClose(prices[i]);
			
			result.add(stockData);
		}
		
		return result;
	}
}
