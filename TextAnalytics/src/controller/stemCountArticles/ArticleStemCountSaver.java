package controller.stemCountArticles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import controller.integration.readAndSplitRawFile.SplitArticle;
import controller.util.BatchRetriever;
import controller.util.GenericRetriever;
import controller.util.Utilities;

import orm.Article;
import orm.ArticleStemCount;
import orm.SessionManager;
import orm.Stem;


public class ArticleStemCountSaver {
	
	private static final String articleSourceParam = "articleSourceId";
	private static final String textParam = "text";
	private static final String stemQueryString = "from Stem where articleSourceId=:" 
			+ articleSourceParam + " and text in (:" + textParam + ")";
	
	public static Article saveStemCountToDatabase(SplitArticle splitArticle, int articleSourceId) {

		Article article = new Article();
		article.setArticleSourceId(articleSourceId);
		article.setFilename(splitArticle.file.getAbsolutePath());
		article.setStartLineNum(splitArticle.startLineNumber);
		article.setAdditionalIdentifier(splitArticle.linesBeforeBody.get(0));
		
		if (splitArticle.articleDate != null) {
			article.setPublishDate(splitArticle.articleDate);
			article.setDayIndex(splitArticle.articleDate);
		}		
		SessionManager.persist(article);

		Map<String, Stem> textStemMap = loadTextStemMap(articleSourceId, splitArticle.stemCountMap.keySet());
		int newStemCount = 0;
		
		for (String stemText : splitArticle.stemCountMap.keySet()) {
			
			final Stem stem;
			if (textStemMap.containsKey(stemText)) {
				stem = textStemMap.get(stemText);
			} else {
				stem = new Stem();
				stem.setArticleSourceId(articleSourceId);
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
	
	
	@SuppressWarnings("rawtypes")
	private static Map<String, Stem> loadTextStemMap(int articleSourceId, Collection<String> stemTextCollection) {
		

		final Query query = SessionManager.createQuery(stemQueryString);
		query.setParameter(articleSourceParam, articleSourceId);

		GenericRetriever<String> gr = new GenericRetriever<String>() {
			@Override
			public List retrieve(Collection<String> coll) {
				query.setParameterList(textParam, coll);
				return query.list();
			}
		};
		BatchRetriever<String> br = new BatchRetriever<>(gr);
		
		List rawList = br.retrieveAll(new ArrayList<>(stemTextCollection));
		List<Stem> stemList = Utilities.convertGenericList(rawList);
		
		Map<String, Stem> result = new HashMap<>();
		
		for (Stem stem : stemList) {
			result.put(stem.getText(), stem);
		}
		
		return result;
	}
}
