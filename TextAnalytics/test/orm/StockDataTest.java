package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

public class StockDataTest {

	@Test
	public void testStockData() {
		SessionManager.setUseForTest(true);
		
		Query query = SessionManager.createQuery("from StockData where company.id=1 order by dayTime asc");
		query.setFirstResult(0);
		query.setMaxResults(10);
		
		@SuppressWarnings("rawtypes")
		List result = query.list();
		assertNotNull(result);
		assertTrue(result.size() > 0);
		
		StockData stockData = (StockData)result.get(0);
		assertNotNull(stockData.getCompany());
		assertNotNull(stockData.getDayTime());
		assertNotNull(stockData.getAdjustedClose());
		assertNotNull(stockData.getClose());
		assertNotNull(stockData.getDayIndex());
		assertNotNull(stockData.getDayTime());
		assertNotNull(stockData.getHigh());
		assertNotNull(stockData.getLow());
		assertNotNull(stockData.getOpen());
		assertNotNull(stockData.getVolume());
	}

}
