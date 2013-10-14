package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.util.Utilities;

public class ArticleDuplicateTest {

	@Test
	public void test() {
		List<ArticleDuplicate> list = Utilities.convertGenericList(SessionManager.createQuery("from ArticleDuplicate").list());
		assertNotNull(list);
		assertTrue(list.size() > 0);
		
		for (int i = 0; i < 10; i++) {
			System.out.println(list.get(i));
		}
	}

}
