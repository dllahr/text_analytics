package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import controller.stockPrices.update.AddOrReplaceDeterminer;
import controller.stockPrices.update.AddOrReplaceInfo;
import controller.stockPrices.update.StockDataAdder;
import controller.stockPrices.update.StockDataReplacer;
import controller.stockPrices.update.AddOrReplaceInfo.AddOrReplace;

public class MainAddOrReplaceStockData {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		final int companyId = Integer.valueOf(args[0]);
		final File stockDataFile = new File(args[1]);
		
		System.out.println("Checking whether new stock data can be added or if all must be replaced");
		BufferedReader stockDataReader = buildReaderSkipHeader(stockDataFile);
		AddOrReplaceInfo info = (new AddOrReplaceDeterminer()).determine(companyId, stockDataReader);
		stockDataReader.close();

		stockDataReader = buildReaderSkipHeader(stockDataFile);
		if (info.addOrReplace == AddOrReplace.onlyAddNew) {
			System.out.println("Attempting to add new stock data only");
			(new StockDataAdder()).addStockData(info.newestInDatabase, stockDataReader);
		} else {
			System.out.println("Replacing all stock data");
			(new StockDataReplacer()).replaceStockData(companyId, stockDataReader);
		}
		
		stockDataReader.close();
	}
	
	static BufferedReader buildReaderSkipHeader(File stockDataFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(stockDataFile));
		
		reader.readLine();

		return reader;
	}

}
