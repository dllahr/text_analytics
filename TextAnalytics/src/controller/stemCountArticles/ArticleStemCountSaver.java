package controller.stemCountArticles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import controller.dateExtractionConversion.ArticleFileDatePair;
import controller.util.Utilities;

import orm.Article;
import orm.ArticleStemCount;
import orm.ScoringModel;
import orm.SessionManager;
import orm.Stem;


public class ArticleStemCountSaver {

	private static final int batchSize = 1000;
	
	private static final String scoringModelParam = "scoringModel";
	private static final String textParam = "text";
	private static final String stemQueryString = "from Stem where scoringModel=:" 
			+ scoringModelParam + " and text in (:" + textParam + ")";
	
	public static Article saveStemCountToDatabase(Map<String, Integer> stemCount, ArticleFileDatePair articleFileDatePair,
			ScoringModel scoringModel) {

		Article article = new Article();
		article.setId(Utilities.getMaxId("Article")+1);
		article.setScoringModel(scoringModel);
		article.setFilename(articleFileDatePair.getFile().getAbsolutePath());
		
		if (articleFileDatePair.getDate() != null) {
			article.setPublishDate(articleFileDatePair.getDate());
			article.setDayIndex(articleFileDatePair.getDate());
		}
		
		SessionManager.persist(article);
		
		int firstNewStemId = Utilities.getMaxId("Stem") + 1;
		int nextStemId = firstNewStemId;
		
		Map<String, Stem> textStemMap = loadTextStemMap(scoringModel, stemCount.keySet());
		
		for (String stemText : stemCount.keySet()) {
			
			final Stem stem;
			if (textStemMap.containsKey(stemText)) {
				stem = textStemMap.get(stemText);
			} else {
				stem = new Stem();
				stem.setId(nextStemId);
				nextStemId++;
				stem.setScoringModel(scoringModel);
				stem.setText(stemText);
				SessionManager.persist(stem);
			}
			
			ArticleStemCount articleStemCount = new ArticleStemCount();
			articleStemCount.setArticle(article);
			articleStemCount.setStem(stem);
			articleStemCount.setCount(stemCount.get(stemText));
			SessionManager.persist(articleStemCount);
		}
		
		if (nextStemId != firstNewStemId) {
			System.out.print(" new stems: " + (nextStemId - firstNewStemId) + " ");
		}
		
		return article;
	}
	
	
	private static Map<String, Stem> loadTextStemMap(ScoringModel scoringModel, Collection<String> stemTextCollection) {
		List<Stem> stemList = new ArrayList<>(stemTextCollection.size());

		Query query = SessionManager.createQuery(stemQueryString);
		query.setParameter(scoringModelParam, scoringModel);

		Iterator<String> stemTextIter = stemTextCollection.iterator();
		while (stemTextIter.hasNext()) {
			List<String> queryList = new LinkedList<>();
			int count = 0;
			
			while (stemTextIter.hasNext() && count < batchSize) {
				queryList.add(stemTextIter.next());
				count++;
			}
			
			query.setParameterList(textParam, queryList);
			List<Stem> queryResult = Utilities.convertGenericList(query.list());
			stemList.addAll(queryResult);
		}
		
		Map<String, Stem> result = new HashMap<>();
		
		for (Stem stem : stemList) {
			result.put(stem.getText(), stem);
		}
		
		return result;
	}
}
