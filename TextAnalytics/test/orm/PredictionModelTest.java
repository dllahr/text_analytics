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
	
	@Test
	public void testBuildFilter() {
		PredictionModel pm = new PredictionModel();
		
		pm.setLowerThreshold(1.1);
		PredictionModel.Filter filter = pm.buildFilter();
		assertTrue(filter.passesFilter(1.2));
		assertTrue(filter.passesFilter(1.1));
		assertFalse(filter.passesFilter(1.0));
		
		pm.setUpperThreshold(2.2);
		filter = pm.buildFilter();
		assertTrue(filter.passesFilter(1.2));
		assertTrue(filter.passesFilter(1.1));
		assertTrue(filter.passesFilter(2.2));
		assertFalse(filter.passesFilter(1.0));
		assertFalse(filter.passesFilter(2.3));
		
		pm.setLowerThreshold(null);
		filter = pm.buildFilter();
		assertTrue(filter.passesFilter(2.2));
		assertTrue(filter.passesFilter(2.1));
		assertFalse(filter.passesFilter(2.3));
	}
}
