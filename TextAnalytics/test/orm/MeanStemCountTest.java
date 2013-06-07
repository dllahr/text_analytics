package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.util.Utilities;

public class MeanStemCountTest {

	@Test
	public void test() {
		@SuppressWarnings("unused")
		List<MeanStemCount> list = Utilities.convertGenericList(SessionManager.createQuery("from MeanStemCount").list());
		assertTrue(true);
	}

}
