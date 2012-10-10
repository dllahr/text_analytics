package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ArticlePcValueTest {

	@Test
	public void test() {
		SessionManager.setUseForTest(true);
		
		@SuppressWarnings("rawtypes")
		List result = UtilitiesForTesting.getFirst10Entities("ArticlePcValue");
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

}
