package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class StemTest {

	@Test
	public void test() {
		Stem stem = new Stem();
		SessionManager.persist(stem);
		
		assertNotNull(stem.getId());
		
		System.out.println(stem.getId());
	}

	@Test
	public void testGetStems() {
		List<Stem> list = Stem.getStemsOrderedById();
		assertTrue(list.size() > 0);
		
		int prevId = list.get(0).getId() - 1;
		for (Stem stem : list) {
			assertTrue(stem.getId() > prevId);
			prevId = stem.getId();
		}
	}
	
	
	@Test
	public void testPostgresqlCreate() {
		Stem stem = new Stem();
		stem.setText("testPostgresqlCreate");
		stem.setStop(false);
		SessionManager.persist(stem);
		
		assertNotNull(stem.getId());
		
		System.out.println(stem.getId());
		
		SessionManager.commit();
	}
}
