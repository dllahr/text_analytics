package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class PredictionTest {

	@Test
	public void test() {
		List<Prediction> result = UtilitiesForTesting.getFirst10Entities("Prediction");
		assertNotNull(result);
//		assertTrue(result.size() > 0);
	}

}
