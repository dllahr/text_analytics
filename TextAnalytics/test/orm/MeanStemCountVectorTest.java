package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.util.Utilities;

public class MeanStemCountVectorTest {

	@Test
	public void test() {
		@SuppressWarnings("unused")
		List<MeanStemCountVector> list = Utilities.convertGenericList(SessionManager.createQuery("from MeanStemCountVector").list());
		assertTrue(true);
	}

}
