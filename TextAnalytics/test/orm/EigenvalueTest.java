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

	@Test
	public void testCreate() {
		Eigenvalue e = new Eigenvalue();
		
		ScoringModel sm = new ScoringModel();
		sm.setId(100);
		SessionManager.persist(sm);
		
		e.setScoringModel(sm);
		e.setSortIndex(0);
		e.setValue(11.1);
		
		SessionManager.persist(e);
		
		assertNotNull(e.getId());
		System.out.println(e.getId());
		
		SessionManager.commit();
	}
}
