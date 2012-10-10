package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class PredictionModelTest {

	@Test
	public void test() {
		@SuppressWarnings("rawtypes")
		List res = UtilitiesForTesting.getFirst10Entities("PredictionModel");
		assertNotNull(res);
		assertTrue(res.size() > 0);
	}

}
