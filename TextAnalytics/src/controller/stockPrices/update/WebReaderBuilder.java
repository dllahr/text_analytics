package controller.stockPrices.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class WebReaderBuilder {
	private static final String urlPrefix = "http://ichart.finance.yahoo.com/table.csv?s=";
	private static final String urlSuffix = "&g=d&ignore=.csv";
	
	private static final String expectedHeaders = "Date,Open,High,Low,Close,Volume,Adj Close";
	
	public static BufferedReader build(String stockSymbol) throws IOException, WebReaderHeaderMismatchException {
		StringBuilder builder = new StringBuilder(urlPrefix);
		builder.append(stockSymbol);
		builder.append(urlSuffix);
		
		URL url = new URL(builder.toString());
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String readHeaders = reader.readLine();
		
		if (! doHeadersMatch(readHeaders)) {
			throw new WebReaderHeaderMismatchException("found:  " + readHeaders + "   expected:  " + expectedHeaders);
		}
		
		return reader;
	}
	
	static boolean doHeadersMatch(String readHeaders) {
		return readHeaders.equalsIgnoreCase(expectedHeaders);
	}
}
