package orm;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class ArticleTest {
	private static final String dateFormatString = "yyyy-MM-dd";

	@Test
	public void test() {
		Article article = new Article();
		SessionManager.persist(article);
		assertNotNull(article.getId());
		
		System.out.println(article.getId());
	}

	@Test
	public void testRetrieveArticleIdsForMinDateAndScoringModel() throws ParseException {
		final DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		Date minDate = dateFormat.parse("2013-08-20");
		
		List<Integer> articleIdList = Article.getArticleIdsForMinDateAndArticleSource(minDate, null, 1, false);
		assertNotNull(articleIdList);
		assertTrue(articleIdList.size() > 0);
		
		for (Integer id : articleIdList) {
			System.out.println(id);
		}
		
		Date maxDate = dateFormat.parse("2013-08-23");
		articleIdList = Article.getArticleIdsForMinDateAndArticleSource(minDate, maxDate, 1, false);
		assertNotNull(articleIdList);
		assertEquals(6, articleIdList.size());
		
		articleIdList = Article.getArticleIdsForMinDateAndArticleSource(minDate, null, 1, true);
		assertNotNull(articleIdList);
	}
	
	@Test
	public void testGetArticles() {
		List<Article> list = Article.getArticlesOrderById(1);
		assertTrue(list.size() > 0);
		
		int prevId = list.get(0).getId() - 1;
		for (Article article : list) {
			assertTrue(article.getId() > prevId);
			prevId = article.getId();
		}
	}
}
