package research.regression.stockprice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import controller.stockPrices.SmoothedStockPrices;

import orm.Company;
import orm.SessionManager;


public class CalculateSmoothedStockPriceChange {
	
	private static final double[] weightsArray = {0.5, 1.0, 0.5};

	private static final int dayOffset = 40;
	
	private static final String delimeter = ",";
	
	public static void main(String args[]) throws IOException {
		final int companyId = Integer.valueOf(args[0]);
		final String dayIndexFilePath = args[1];
		
		System.out.println("Calculate Stock Price Change");
		System.out.println("company ID:  " + companyId);
		System.out.println("day index file path:  " + dayIndexFilePath);
		
		List<Integer> dayIndexList = readDayIndexSet(dayIndexFilePath);
		final int minDayIndex = Collections.min(dayIndexList) - weightsArray.length - 7;

		final Company company = (Company)SessionManager.createQuery("from Company where id = :id").setInteger("id", companyId).list().get(0);
		
		SmoothedStockPrices smoothedStockPrices = new SmoothedStockPrices(minDayIndex, company, weightsArray);
		
		for (Integer dayIndex : dayIndexList) {
			double initialPrice = smoothedStockPrices.getSmoothedPriceClosestAfterDayIndex(dayIndex);
			double offsetPrice = smoothedStockPrices.getSmoothedPriceClosestAfterDayIndex(dayIndex + dayOffset);
			double fracChange = offsetPrice / initialPrice;
			
			System.out.println(dayIndex + delimeter + fracChange);
		}
	}
	
	private static List<Integer> readDayIndexSet(String dayIndexFilePath) throws IOException {
		List<Integer> dayIndexSet = new LinkedList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(dayIndexFilePath));
		String curLine;
		while ((curLine = reader.readLine()) != null) {
			dayIndexSet.add(Integer.valueOf(curLine));
		}
		reader.close();
		
		return dayIndexSet;
	}
}
