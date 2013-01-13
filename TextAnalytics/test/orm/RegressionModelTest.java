package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.util.Utilities;

public class RegressionModelTest {

	@Test
	public void test() {		
		List<RegressionModel> list = 
				Utilities.convertGenericList(SessionManager.createQuery("from RegressionModel").list());
		
		RegressionModel model = list.get(0);
		
		assertNotNull(model.getId());
		assertNotNull(model.getCompany());
	}

}
