package controller.stockUpdate;

import static org.junit.Assert.*;

import org.junit.Test;

public class DownloadStockDataTest {
	private static final String stockSymbol = "CAT";
	
	@Test
	public void test() {
		DownloadStockData downloadStockData = new DownloadStockData(stockSymbol);
		assertTrue(downloadStockData.hasNext());
		for (int i = 0; i < 3; i++) {
			assertNotNull(downloadStockData.next());
		}
		downloadStockData.close();
	}

}
