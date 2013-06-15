package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.util.Utilities;

public class PredictionModelTest {

	@Test
	public void test() {
		List<PredictionModel> list = 
				Utilities.convertGenericList(SessionManager.createQuery("from PredictionModel").list());
		assertNotNull(list);
		assertTrue(list.size() > 0);
		
		System.out.println(list.get(0));
	}

	@Test
	public void testCoefSet() {
		List<PredictionModel> list = 
				Utilities.convertGenericList(SessionManager.createQuery("from PredictionModel").list());
		
		assertNotNull(list);
		assertTrue(list.size() > 0);
		
		PredictionModel pm = list.get(0);
		assertNotNull(pm.getStockSmoothingCoefSet());
		
		for (PredictionModelStockSmoothingCoef coef : pm.getStockSmoothingCoefSet()) {
			System.out.println(coef);
		}
	}
}
