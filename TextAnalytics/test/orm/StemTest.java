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
		List<Stem> list = Stem.getStemsOrderedById(1);
		assertTrue(list.size() > 0);
		
		int prevId = list.get(0).getId() - 1;
		for (Stem stem : list) {
			assertTrue(stem.getId() > prevId);
			prevId = stem.getId();
		}
	}
}
