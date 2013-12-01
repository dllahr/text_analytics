package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ArticleStemCountTest {

	@Test
	public void test() {
		SessionManager.setUseForTest(true);
		
		@SuppressWarnings("rawtypes")
		List result = UtilitiesForTesting.getFirst10Entities("ArticleStemCount");
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testCreate() {
		ArticleStemCount asc = new ArticleStemCount();
		asc.setArticle(Article.getArticlesOrderById(4).get(0));
		asc.setStem(Stem.getStemsOrderedById().get(0));
		asc.setCount(-5);
		
		SessionManager.persist(asc);
		SessionManager.commit();
	}
}
