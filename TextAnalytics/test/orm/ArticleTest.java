package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ArticleTest {

	@Test
	public void test() {
		SessionManager.setUseForTest(true);
		
		@SuppressWarnings("rawtypes")
		List result = SessionManager.createQuery("from Article").list();
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

}
