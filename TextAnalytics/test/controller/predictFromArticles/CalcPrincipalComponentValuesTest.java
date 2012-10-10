package controller.predictFromArticles;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

import controller.predictFromArticles.CalcPrincipalComponentValues;

import orm.Article;
import orm.SessionManager;
import orm.UtilitiesForTesting;

public class CalcPrincipalComponentValuesTest extends CalcPrincipalComponentValues {
	@Test
	public void test() {
		SessionManager.setUseForTest(true);
		
		//should check here to make sure there is data in the database needed to run this calculation
		List<Article> articleList = UtilitiesForTesting.getFirst10Entities("Article");
		assertNotNull(articleList);
		assertTrue(articleList.size() > 0);
		
		List<Integer> articleIdList = new ArrayList<>(articleList.size());
		for (Article article : articleList) {
			articleIdList.add(article.getId());
		}
		
		Query clearQuery = SessionManager.createQuery("delete ArticlePcValue where article.id in (:articleIdList)");
		clearQuery.setParameterList("articleIdList", articleIdList);
		clearQuery.executeUpdate();

		CalcPrincipalComponentValues.calcPrincipalComponentValuesForArticles(articleList);

		SessionManager.commit();
		
		@SuppressWarnings("rawtypes")
		List result = SessionManager.createQuery("from ArticlePcValue").list();
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

}
