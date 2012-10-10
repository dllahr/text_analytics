package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

public class EigenvectorValueTest {

	@Test
	public void test() {
		Query query = SessionManager.createQuery("from EigenvectorValue");
		query.setFirstResult(0);
		query.setMaxResults(10);
		
		@SuppressWarnings("rawtypes")
		List res = query.list();
		assertTrue(res.size() > 0);
	}

}
