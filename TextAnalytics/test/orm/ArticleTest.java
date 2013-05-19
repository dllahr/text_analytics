package orm;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArticleTest {

	@Test
	public void test() {
		Article article = new Article();
		SessionManager.persist(article);
		assertNotNull(article.getId());
		
		System.out.println(article.getId());
	}

}
