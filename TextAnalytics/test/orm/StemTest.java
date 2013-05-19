package orm;

import static org.junit.Assert.*;

import org.junit.Test;

public class StemTest {

	@Test
	public void test() {
		Stem stem = new Stem();
		SessionManager.persist(stem);
		
		assertNotNull(stem.getId());
		
		System.out.println(stem.getId());
	}

}
