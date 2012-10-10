package controller.stockUpdate;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

class DownloadStockData implements Iterator<String> {
	//http://ichart.finance.yahoo.com/table.csv?s=MCD&a=00&b=2&c=1970&d=07&e=8&f=2012&g=d&ignore=.csv
	
	private static final String urlPrefix = "http://ichart.finance.yahoo.com/table.csv?s=";
//	private static final String urlSuffix = "&d=0&e=22&f=2012&g=d&a=0&b=2&c=1962&ignore=.csv";
	
	private static final String urlSuffixMonth = "&d=";
	private static final String urlSuffixDay = "&e=";
	private static final String urlSuffxYear = "&f=";
	private static final String urlSuffix = "&g=d&a=0&b=2&c=1962&ignore=.csv";
	//http://ichart.finance.yahoo.com/table.csv?s=CAT&d=0&e=31&f=2012&g=d&a=0&b=2&c=1962&ignore=.csv
	
	private BufferedInputStream stream;
	
	private int val;
	
	public DownloadStockData(String stockSymbol) {
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
		String[] dateStr = df.format(new Date()).split("-");
		
		StringBuilder builder = new StringBuilder();
		builder.append(urlPrefix).append(stockSymbol).append(urlSuffixMonth).append(dateStr[0]);
		builder.append(urlSuffixDay).append(dateStr[1]).append(urlSuffxYear).append(dateStr[2]);
		builder.append(urlSuffix);
		
		try {
			
			stream = new BufferedInputStream((new URL(builder.toString())).openStream());
			val = stream.read();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			stream = null;
			val = -1;
		} catch (IOException e) {
			e.printStackTrace();
			stream = null;
			val = -1;
		}
	}
	
	public void close() {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("DownloadStockData next() failed to clsoe stream at end", e);
			}
		}
	}


	@Override
	public boolean hasNext() {
		return val != -1;
	}

	@Override
	public String next() {
		if (-1 == val) {
			return null;
		} else {
			StringBuffer buffer = new StringBuffer();
			char valChar;
			while (val != -1 && (valChar = (char)val) != '\n' && valChar != '\r') {
				buffer.append(valChar);
				try {
					val = stream.read();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException("DownloadStockData next() failed to read from stream", e);
				}
			}
			
			if (-1 == val) {
				close();
			} else {
				try {
					val = stream.read();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException("DownloadStockData next() failed to read from stream", e);
				}
			}
			
			return buffer.toString();
		}
	}

	@Override
	public void remove() {
		throw new RuntimeException("DownloadStockData remove() not implemented");
	}
}
