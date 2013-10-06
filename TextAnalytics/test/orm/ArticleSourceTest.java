package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

import controller.util.Utilities;

public class ArticleSourceTest {

	@Test
	public void test() {
		Query query = SessionManager.createQuery("from ArticleSource order by id");
		List<ArticleSource> list = Utilities.convertGenericList(query.list());
		assertNotNull(list);
		
		for (ArticleSource as : list) {
			System.out.println(as);
		}
	}

}
