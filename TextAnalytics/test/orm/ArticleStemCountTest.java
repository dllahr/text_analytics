package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ArticleStemCountTest {

	@Test
	public void test() {
		SessionManager.setUseForTest(true);
		
		@SuppressWarnings("rawtypes")
		List result = UtilitiesForTesting.getFirst10Entities("ArticleStemCount");
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

}
