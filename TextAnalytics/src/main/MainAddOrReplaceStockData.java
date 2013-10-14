package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import orm.Company;

import controller.stockPrices.update.AddOrReplaceDeterminer;
import controller.stockPrices.update.AddOrReplaceInfo;
import controller.stockPrices.update.StockDataAdder;
import controller.stockPrices.update.StockDataReplacer;
import controller.stockPrices.update.AddOrReplaceInfo.AddOrReplace;
import controller.stockPrices.update.WebReaderBuilder;
import controller.stockPrices.update.WebReaderHeaderMismatchException;
import controller.util.CommandLineParser;
import controller.util.CommandLineParserUnrecognizedTokenException;

public class MainAddOrReplaceStockData {

	private static final String companyIdOption = "-companyId";
	private static final String fileOption = "-file";

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws CommandLineParserUnrecognizedTokenException 
	 * @throws WebReaderHeaderMismatchException 
	 */
	public static void main(String[] args) throws IOException, ParseException, 
	CommandLineParserUnrecognizedTokenException, WebReaderHeaderMismatchException {
		
		Map<Integer, StockDataReaderBuilder> companyIdStockDataReaderMap = new HashMap<Integer, StockDataReaderBuilder>();
		
		if (args.length > 0) {
			Map<String, String> argMap = CommandLineParser.parse(args, 0);
			
			if (argMap.containsKey(companyIdOption) && argMap.containsKey(fileOption)) {
				final int companyId = Integer.valueOf(argMap.get(companyIdOption));
				final File stockDataFile = new File(argMap.get(fileOption));
				
				System.out.println("Command line arguments to read from file:  " + companyId + " " + stockDataFile.getName());
				
				companyIdStockDataReaderMap.put(companyId, new StockDataReaderBuilder() {
					@Override
					public BufferedReader build() throws IOException {
						return buildReaderSkipHeader(stockDataFile);
					}
				});
			} else {
				System.err.println("need to have both these options present or neither:  " + companyIdOption + " " + fileOption);
				return;
			}
		} else {
			System.out.println("No command line arguments - will attempt to update all stock data from web");
			populateMapFromDatabase(companyIdStockDataReaderMap);
		}

		
		for (int companyId : companyIdStockDataReaderMap.keySet()) {
			System.out.println("company:  " + companyId);

			StockDataReaderBuilder builder = companyIdStockDataReaderMap.get(companyId);

			System.out.println("Checking whether new stock data can be added or if all must be replaced");
			BufferedReader stockDataReader = builder.build();
			AddOrReplaceInfo info = (new AddOrReplaceDeterminer()).determine(companyId, stockDataReader);
			stockDataReader.close();

			stockDataReader = builder.build();
			if (info.addOrReplace == AddOrReplace.onlyAddNew) {
				System.out.println("Attempting to add new stock data only");
				(new StockDataAdder()).addStockData(info.newestInDatabase, stockDataReader);
			} else {
				System.out.println("Replacing all stock data");
				(new StockDataReplacer()).replaceStockData(companyId, stockDataReader);
			}

			stockDataReader.close();
			System.out.println();
		}
	}
	
	private static void populateMapFromDatabase(Map<Integer, StockDataReaderBuilder> companyIdStockDataReaderMap) {

		for (Company company : Company.findAll()) {
			if (company.getStockSymbol() != null) {
				StockDataReaderBuilder builder = new StockDataWebReaderBuilder(company.getStockSymbol());
				
				companyIdStockDataReaderMap.put(company.getId(), builder);
			}
		}
	}

	static BufferedReader buildReaderSkipHeader(File stockDataFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(stockDataFile));
		
		reader.readLine();

		return reader;
	}
	
	
	static interface StockDataReaderBuilder {
		BufferedReader build() throws IOException, WebReaderHeaderMismatchException;
	}

	static class StockDataWebReaderBuilder implements StockDataReaderBuilder {
		private final String stockSymbol;

		public StockDataWebReaderBuilder(String stockSymbol) {
			this.stockSymbol = stockSymbol;
		}

		@Override
		public BufferedReader build() throws IOException, WebReaderHeaderMismatchException {
			return WebReaderBuilder.build(stockSymbol);
		}
	}
	
	
}
