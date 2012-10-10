package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class PrincipalComponentTest {

	@Test
	public void test() {
		SessionManager.setUseForTest(true);
		
		@SuppressWarnings("rawtypes")
		List result = UtilitiesForTesting.getFirst10Entities("PrincipalComponent");
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

}
