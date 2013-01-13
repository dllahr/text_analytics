package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.util.Utilities;

public class RegressionModelCoefTest {

	@Test
	public void test() {
		List<RegressionModelCoef> list = Utilities.convertGenericList(SessionManager.createQuery("from RegressionModelCoef").list());
		assertTrue(list.size() > 0);
		
		RegressionModelCoef rmc = list.get(0);
		assertNotNull(rmc);
		assertNotNull(rmc.getId());
		assertNotNull(rmc.getRegressionModel());
		
		
	}

}
