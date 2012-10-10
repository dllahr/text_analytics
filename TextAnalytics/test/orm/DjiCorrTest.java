package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.util.Utilities;

public class DjiCorrTest {

	@Test
	public void test() {
		List<DjiCorr> list = Utilities.convertGenericList(SessionManager.createQuery("from DjiCorr").list());
		
		assertTrue(list.size() > 0);
	}

}
