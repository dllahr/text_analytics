package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

public class StockPriceChangeTest {

	@Test
	public void test() {
		SessionManager.setUseForTest(true);
		
		Query query = SessionManager.createQuery("from StockPriceChange");
		query.setFirstResult(0);
		query.setMaxResults(10);
		
		@SuppressWarnings("rawtypes")
		List result = query.list();
		assertNotNull(result);
	}

}
