package controller.stemCountArticles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import controller.articleIntegration.readAndSplitRawFile.SplitArticle;
import controller.util.BatchRetriever;
import controller.util.GenericRetriever;
import controller.util.Utilities;

import orm.Article;
import orm.ArticleStemCount;
import orm.SessionManager;
import orm.Stem;


public class ArticleStemCountSaver {

	private static final String textParam = "text";
	private static final String stemQueryString = "from Stem where text in (:" + textParam + ")";
	
	private final Map<String, Stem> textStemMap;
	private final boolean useIndividualMaps;
	
	public ArticleStemCountSaver(boolean useIndividualMaps) {
		this.useIndividualMaps = useIndividualMaps;
		
		if (useIndividualMaps) {
			textStemMap = null;
		} else {
			System.out.print("Loading map of all stems:  ");
			textStemMap = loadAllTextStemMap();
			System.out.println("done");
		}
	}
	
	public Article saveStemCountToDatabase(SplitArticle splitArticle, int articleSourceId) {

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

		Map<String, Stem> currentTextStemMap = useIndividualMaps ? 
				loadTextStemMap(splitArticle.stemCountMap.keySet()) : textStemMap;		
		
		int newStemCount = 0;
		
		for (String stemText : splitArticle.stemCountMap.keySet()) {
			
			final Stem stem;
			if (currentTextStemMap.containsKey(stemText)) {
				stem = currentTextStemMap.get(stemText);
			} else {
				stem = new Stem();
				stem.setText(stemText);
				SessionManager.persist(stem);
				
				if (!useIndividualMaps) {
					SessionManager.merge(stem);
					textStemMap.put(stemText, stem);
				}
				
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
	private static Map<String, Stem> loadTextStemMap(Collection<String> stemTextCollection) {
	
		final Query query = SessionManager.createQuery(stemQueryString);

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
		
		return buildTextStemMap(stemList);
	}
	
	private static Map<String, Stem> buildTextStemMap(List<Stem> stemList) {
		Map<String, Stem> result = new HashMap<>();

		for (Stem stem : stemList) {
			result.put(stem.getText(), stem);
		}

		return result;
	}
	
	private static Map<String, Stem> loadAllTextStemMap() {
		List<Stem> stemList = Utilities.convertGenericList(SessionManager.createQuery("from Stem").list());
		
		return buildTextStemMap(stemList);
	}
}
