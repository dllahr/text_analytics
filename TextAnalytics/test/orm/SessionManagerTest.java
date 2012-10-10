package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import orm.SessionManager;

public class SessionManagerTest {
	private static final String nonTestUsername = "personal";
	private static final String testUsername = "tester";
	
	@Test
	public void testSimpleQuery() {
		SessionManager.setUseForTest(false);
		simpleQueryTest();
		userTest(nonTestUsername);
		
		SessionManager.setUseForTest(true);
		simpleQueryTest();
		userTest(testUsername);
		
		SessionManager.setUseForTest(false);
		simpleQueryTest();
		userTest(nonTestUsername);
	}
	
	private static void simpleQueryTest() {
		final String testMessage = "hello world";
		@SuppressWarnings("rawtypes")
		List objList = SessionManager.createSqlQuery("select '"+testMessage+"' from dual").list();
		assertTrue(testMessage.equals(objList.get(0).toString()));
	}
	
	private static void userTest(final String username) {
		@SuppressWarnings("unchecked")
		List<String> userList = SessionManager.createSqlQuery("select user from dual").list();
		assertNotNull(userList);
		assertTrue(userList.size() == 1);
		assertTrue(username.equalsIgnoreCase(userList.get(0)));
	}
}
