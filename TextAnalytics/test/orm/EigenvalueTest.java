package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

public class EigenvalueTest {

	@Test
	public void test() {
		SessionManager.setUseForTest(true);
		
		Query query = SessionManager.createQuery("from Eigenvalue");
		query.setFirstResult(0);
		query.setMaxResults(10);
		
		@SuppressWarnings("rawtypes")
		List objList = query.list();
		assertNotNull(objList);
//		assertTrue(objList.size() > 0);
	}

}
