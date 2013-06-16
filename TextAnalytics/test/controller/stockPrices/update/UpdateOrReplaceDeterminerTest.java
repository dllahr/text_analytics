package controller.stockPrices.update;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import controller.stockPrices.update.AddOrReplaceInfo.AddOrReplace;

import orm.StockData;

public class UpdateOrReplaceDeterminerTest {

	@Test
	public void testGetNewestStockData() {
		StockData result = AddOrReplaceDeterminer.getNewestStockData(1);
		assertNotNull(result);
		System.out.println(result.getDayIndex());
		
		result = AddOrReplaceDeterminer.getNewestStockData(-1);
		assertNull(result);
	}

	@Test
	public void testDetermineFromStockData() throws IOException, ParseException {
		final String filePath = "test/resources/updateOrReplaceDeterminerData.csv";
		StockData stockData = new StockData();
		stockData.setDayIndex(15826);
		stockData.setAdjustedClose(1.1);
		
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		AddOrReplace addOrRep = AddOrReplaceDeterminer.determineFromStockData(stockData, reader);
		assertNotNull(addOrRep);
		assertEquals(AddOrReplace.onlyAddNew, addOrRep);
		reader.close();
		
		stockData.setAdjustedClose(1.101);
		reader = new BufferedReader(new FileReader(filePath));
		addOrRep = AddOrReplaceDeterminer.determineFromStockData(stockData, reader);
		assertNotNull(addOrRep);
		assertEquals(AddOrReplace.replaceAll, addOrRep);
		reader.close();
		
		int[] failDayIndexes = {15825, 14000};
		for (int failDayIndex : failDayIndexes) {
			stockData.setDayIndex(failDayIndex);
			
			reader = new BufferedReader(new FileReader(filePath));
			
			boolean wasThrown = false;
			try {
				addOrRep = AddOrReplaceDeterminer.determineFromStockData(stockData, reader);
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
				wasThrown = true;
			}
			assertTrue("expected exception to be thrown", wasThrown);
			
			reader.close();
		}
	}

}
