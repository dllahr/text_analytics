package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

public class CompanyTest {

	@Test
	public void test() {
		SessionManager.setUseForTest(true);
		
		Query query = SessionManager.createQuery("from Company");
		query.setFirstResult(0);
		query.setMaxResults(10);
		@SuppressWarnings("rawtypes")
		List listObj = query.list();
		assertNotNull(listObj);
		assertTrue("some companies (not zero) returned by query", listObj.size() > 0); 
		
		
		Company company = (Company)listObj.get(0);
		assertNotNull(company.getId());
		assertNotNull(company.getName());
		assertNotNull(company.getStockSymbol());
//		assertNotNull(company.getStockDataList());
	}

}
