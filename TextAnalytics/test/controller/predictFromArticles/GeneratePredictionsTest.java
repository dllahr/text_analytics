package controller.predictFromArticles;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

import orm.Article;
import orm.Prediction;
import orm.PredictionModel;
import orm.SessionManager;
import orm.UtilitiesForTesting;

import controller.predictFromArticles.GeneratePredictions;
import controller.util.Utilities;

public class GeneratePredictionsTest extends GeneratePredictions {

	@Test
	public void testQueryStr() {
		SessionManager.setUseForTest(true);
		
		Query artIdQuery = SessionManager.createQuery("select id from Article");
		artIdQuery.setFirstResult(0);
		artIdQuery.setMaxResults(10);
		List<Integer> artIdList = Utilities.convertGenericList(artIdQuery.list());
		
		Query query = SessionManager.createQuery(GeneratePredictions.baseQueryStr + GeneratePredictions.lowerThresholdCondition);
		query.setParameterList(GeneratePredictions.articleIdParamName, artIdList);
		List<Object[]> result = Utilities.convertGenericList(query.list());
		assertNotNull(result);
		assertTrue(result.size() > 0);
		
		for (Object[] row : result) {
			assertTrue(row[0] instanceof PredictionModel);
			System.out.print(((PredictionModel)row[0]).getId() + " ");
			
			assertTrue(row[1] instanceof Article);
			System.out.print(((Article)row[1]).getId() + " ");
			
			assertNotNull(row[2]);
			assertNotNull(row[3]);
			System.out.println(row[2] + " " + row[3]);
		}
	}
	
	@Test
	public void testFull() {
		SessionManager.setUseForTest(true);
		List<Article> articleList = UtilitiesForTesting.getFirst10Entities("Article");
		
		GeneratePredictions.generatePredictions(articleList);
		
		List<Prediction> result = Utilities.convertGenericList(SessionManager.createQuery("from Prediction").list());
		assertTrue(result.size() > 0);
		Collections.sort(result, new Comparator<Prediction>() {
			@Override
			public int compare(Prediction o1, Prediction o2) {
				if (o1.getArticle().getId() != o2.getArticle().getId()) {
					return o1.getArticle().getId().compareTo(o2.getArticle().getId());
				} else if (o1.getPredictionModel().getId() != o2.getPredictionModel().getId()) {
					return o1.getPredictionModel().getId().compareTo(o2.getPredictionModel().getId());
				} else {
					return o1.getDayTime().compareTo(o2.getDayTime());
				}
			}
		});
		
		DateFormat df = new SimpleDateFormat("dd-MMM-yy");
		for (Prediction prediction : result) {
			System.out.println(prediction.getArticle().getId() + " " + prediction.getPredictionModel().getId() + " " 
					+ df.format(prediction.getDayTime()) + " " + prediction.getStockPrice());
		}
	}

}
