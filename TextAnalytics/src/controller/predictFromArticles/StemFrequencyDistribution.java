package controller.predictFromArticles;

import gate.Annotation;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.FeatureMap;
import gate.corpora.CorpusImpl;
import gate.corpora.DocumentContentImpl;
import gate.corpora.DocumentImpl;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;
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

import tools.TextFileReader;

public class StemFrequencyDistribution {
	
	private static final String gateHome = "C:\\no_backup\\bin\\gate";

	private static final String stemmerFileUrl = "resources/stemmer";

	private static final long millisPerDay = 24*60*60*1000;
			//"C:\\no_backup\\personal\\projects\\text_analytics\\gate\\stemmer";
	
	private static final String companyParam = "company";
	private static final String textParam = "text";
	private static final String stemQueryString = "from Stem where company=:" 
			+ companyParam + " and text in (:" + textParam + ")";
	/**
	 * each file in the inputDir directory is assumed to contain an individual article.  
	 * For each, identify the stems and get the count of
	 * each stem. Save the results to the database.  Return
	 * a list of orm objects representing the articles
	 * 
	 * @param inputDir
	 * @throws GateException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static List<Article> calculateAndSaveArticleStems(final List<ArticleFileDatePair> articlesWithDatesList, final ScoringModel company) 
			throws GateException, IOException {
		
		final List<Article> articleList = new LinkedList<>();

		System.out.println("gate installation directory: " + gateHome);
		if (gate.Gate.getGateHome() == null) {
			gate.Gate.setGateHome(new File(gateHome));
		}
		if (! gate.Gate.isInitialised()) {
			gate.Gate.init();
		}
		
		System.out.println("gate stemmer (JAPE):  " + stemmerFileUrl);
		CorpusController controller = 
			(CorpusController)PersistenceManager.loadObjectFromFile(new File(stemmerFileUrl));

		Corpus corpus = new CorpusImpl();
		
		controller.setCorpus(corpus);
		
		for (ArticleFileDatePair articleFileDatePair : articlesWithDatesList) {
			File curFile = articleFileDatePair.getFile();

			System.out.print(curFile.getName());

			Document document = new DocumentImpl();
			document.setName(curFile.getName());

			String content = TextFileReader.readFile(curFile);

			DocumentContentImpl docContent = new DocumentContentImpl(content);
			document.setContent(docContent);
			corpus.add(document);

			controller.execute();

			final HashMap<String, Counter> stemCount = calculateStemCount(document);

			Article article = saveStemCountToDatabase(stemCount, articleFileDatePair, company);
			articleList.add(article);

			corpus.remove(document);

			System.out.println(" done");
		}
		
		return articleList;
	}
	
	
	private static HashMap<String, Counter> calculateStemCount(Document document) {
		HashMap<String, Counter> stemCount = new HashMap<String, Counter>();
		for (Annotation curAnnot : document.getAnnotations()) {
			FeatureMap curFeatureMap = curAnnot.getFeatures();
			if (curFeatureMap != null) {
				Object kind = curFeatureMap.get("kind");
				if (kind != null && kind.toString().equalsIgnoreCase("word")) {
					Object stem = curFeatureMap.get("stem");
					
					if (stem != null) {
						Counter curCount = stemCount.get(stem.toString());
						if (null == curCount) {
							curCount = new Counter();
							stemCount.put(stem.toString(), curCount);
						}
						curCount.increaseCount();
					}	
				}
			}
		}
		
		return stemCount;
	}
	
	private static Article saveStemCountToDatabase(Map<String, Counter> stemCount, ArticleFileDatePair inputFileWithDate, ScoringModel company) {
		Article article = new Article();
		article.setId(Utilities.getMaxId("Article")+1);
		article.setScoringModel(company);
		article.setFilename(inputFileWithDate.getFile().getAbsolutePath());
		
		if (inputFileWithDate.getDate() != null) {
			article.setPublishDate(inputFileWithDate.getDate());
			article.setDayIndex((int)(inputFileWithDate.getDate().getTime() / millisPerDay));
		}
		
		SessionManager.persist(article);
		
		int firstNewStemId = Utilities.getMaxId("Stem") + 1;
		int nextStemId = firstNewStemId;
		
		Map<String, Stem> textStemMap = loadTextStemMap(company, stemCount.keySet());
		
		for (String stemText : stemCount.keySet()) {
			
			final Stem stem;
			if (textStemMap.containsKey(stemText)) {
				stem = textStemMap.get(stemText);
			} else {
				stem = new Stem();
				stem.setId(nextStemId);
				nextStemId++;
				stem.setScoringModel(company);
				stem.setText(stemText);
				SessionManager.persist(stem);
			}
			
			ArticleStemCount articleStemCount = new ArticleStemCount();
			articleStemCount.setArticle(article);
			articleStemCount.setStem(stem);
			articleStemCount.setCount(stemCount.get(stemText).getCount());
			SessionManager.persist(articleStemCount);
		}
		
		if (nextStemId != firstNewStemId) {
			System.out.print(" new stems: " + (nextStemId - firstNewStemId-1) + " ");
		}
		
		return article;
	}
	
	private static final int batchSize = 1000;
	private static Map<String, Stem> loadTextStemMap(ScoringModel company, Collection<String> stemTextCollection) {
		List<Stem> stemList = new ArrayList<>(stemTextCollection.size());

		Query query = SessionManager.createQuery(stemQueryString);
		query.setParameter(companyParam, company);

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
