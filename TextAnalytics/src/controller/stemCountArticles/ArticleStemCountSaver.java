package controller.stemCountArticles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import controller.integration.readAndSplitRawFile.SplitArticle;
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
	
	public static Article saveStemCountToDatabase(SplitArticle splitArticle, ScoringModel scoringModel) {

		Article article = new Article();
		article.setScoringModel(scoringModel);
		article.setFilename(splitArticle.file.getAbsolutePath());
		article.setStartLineNum(splitArticle.startLineNumber);
		article.setAdditionalIdentifier(splitArticle.linesBeforeBody.get(0));
		
		if (splitArticle.articleDate != null) {
			article.setPublishDate(splitArticle.articleDate);
			article.setDayIndex(splitArticle.articleDate);
		}		
		SessionManager.persist(article);

		Map<String, Stem> textStemMap = loadTextStemMap(scoringModel, splitArticle.stemCountMap.keySet());
		int newStemCount = 0;
		
		for (String stemText : splitArticle.stemCountMap.keySet()) {
			
			final Stem stem;
			if (textStemMap.containsKey(stemText)) {
				stem = textStemMap.get(stemText);
			} else {
				stem = new Stem();
				stem.setScoringModel(scoringModel);
				stem.setText(stemText);
				SessionManager.persist(stem);
				
				newStemCount++;
			}
			
			ArticleStemCount articleStemCount = new ArticleStemCount();
			articleStemCount.setArticle(article);
			articleStemCount.setStem(stem);
			articleStemCount.setCount(splitArticle.stemCountMap.get(stemText));
			SessionManager.persist(articleStemCount);
		}
		
		if (newStemCount > 0) {
			System.out.println(" new stems: " + newStemCount);
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
