package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.util.Utilities;

public class ActivityTest {

	@Test
	public void test() {
		List<Activity> result = Utilities.convertGenericList(SessionManager.createQuery("from Activity").list());
		assertTrue(result.size() > 0);
	}

}
