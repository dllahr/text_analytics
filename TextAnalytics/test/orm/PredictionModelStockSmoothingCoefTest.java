package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.util.Utilities;

public class PredictionModelStockSmoothingCoefTest {

	@Test
	public void test() {
		List<PredictionModelStockSmoothingCoef> list = 
				Utilities.convertGenericList(SessionManager.createQuery("from PredictionModelStockSmoothingCoef").list());
		
		assertNotNull(list);
		
		int i = 0;
		for (PredictionModelStockSmoothingCoef coef : list) {
			System.out.println(coef);
			
			i++;
			
			if (10 == i) {
				break;
			}
		}
	}

}
