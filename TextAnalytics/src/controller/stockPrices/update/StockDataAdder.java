package controller.stockPrices.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;

import orm.SessionManager;
import orm.StockData;

public class StockDataAdder extends StockDataBuilder {

	public StockDataAdder() {
	}

	public void addStockData(StockData newestInDatabase, BufferedReader stockDataReader) throws IOException, ParseException {
		
		String curLine = null;

		boolean doContinue = true;
		int count = 0;
		while (doContinue && (curLine = stockDataReader.readLine()) != null) {
			String[] lineArray = curLine.split(InputFormatConstants.delimeter);
			
			StockData stockData = new StockData();
			stockData.setDayTimeAndDayIndex(parseDate(lineArray));
			
			if (stockData.getDayIndex() > newestInDatabase.getDayIndex()) {
				stockData.setCompany(newestInDatabase.getCompany());
				
				updateStockDataFieldsFromLine(lineArray, stockData);

				SessionManager.persist(stockData);
				
				count++;
				
				if (count%1000 == 0) {
					System.out.println("Commiting stock data.  count:  " + count);
					SessionManager.commit();
				}	
			} else {
				doContinue = false;
			}
		}

		if (0 == count) {
			System.out.println("first entry in BufferedReader stockDataReader was same date or older than database - nothing added");
		} else {
			if (count%1000 != 0) {
				System.out.println("Commiting stock data.  count:  " + count);
				SessionManager.commit();
			}
		
			if (null == curLine) {
				System.err.println("StockDataAdder addStockData Warning:  BufferedReader stockDataReader ended before reaching day index that matched database");
			}
		}
		
	}
}
