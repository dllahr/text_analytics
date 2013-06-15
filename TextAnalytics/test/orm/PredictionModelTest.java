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
		
		System.out.println(list.get(0));
	}

}
