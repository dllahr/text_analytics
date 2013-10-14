package orm;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import controller.util.Utilities;

public class ArticleTest {

	@Test
	public void test() {
		Article article = new Article();
		SessionManager.persist(article);
		assertNotNull(article.getId());
		
		System.out.println(article.getId());
	}

	@Test
	public void testRetrieveArticleIdsForMinDateAndScoringModel() throws ParseException {
		Date minDate = Utilities.dateFormat.parse("2013-08-20");
		
		List<Integer> articleIdList = Article.getArticleIdsForMinDateAndArticleSource(minDate, null, 1, false, false);
		assertNotNull(articleIdList);
		assertTrue(articleIdList.size() > 0);
		
		for (Integer id : articleIdList) {
			System.out.println(id);
		}
		
		Date maxDate = Utilities.dateFormat.parse("2013-08-23");
		articleIdList = Article.getArticleIdsForMinDateAndArticleSource(minDate, maxDate, 1, false, false);
		assertNotNull(articleIdList);
		assertEquals(6, articleIdList.size());
		
		articleIdList = Article.getArticleIdsForMinDateAndArticleSource(minDate, null, 1, true, false);
		assertNotNull(articleIdList);
	}
	
	@Test
	public void testRetrieveArticleIdsForMinDateAndScoringModelExcludeDuplicates() throws ParseException {
		Date date = Utilities.dateFormat.parse("2012-04-15");
		
		List<Integer> articleIdList = Article.getArticleIdsForMinDateAndArticleSource(date, date, 2, false, true);
		assertNotNull(articleIdList);
		assertTrue(articleIdList.size() > 0);
		
		for (Integer id : articleIdList) {
			System.out.println(id);
		}
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
