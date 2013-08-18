package controller.stemCountArticles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.integration.readAndSplitRawFile.SplitArticle;
import controller.util.Counter;

import gate.Annotation;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.FeatureMap;
import gate.corpora.CorpusImpl;
import gate.corpora.DocumentContentImpl;
import gate.corpora.DocumentImpl;
import gate.creole.ExecutionException;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

public class StemCounter {
	private final int batchSize;
	
	private static final String gateHome = "C:\\no_backup\\bin\\gate";

	private static final String stemmerFileUrl = "resources/stemmer";
	
	private final Corpus corpus;
	
	private final CorpusController controller;
	
	public StemCounter(int batchSize) throws GateException, IOException {
		this.batchSize = batchSize;

		System.out.println("gate installation directory: " + gateHome);
		if (gate.Gate.getGateHome() == null) {
			gate.Gate.setGateHome(new File(gateHome));
		}
		if (! gate.Gate.isInitialised()) {
			gate.Gate.init();
		}
		
		System.out.println("gate stemmer (JAPE):  " + stemmerFileUrl);
		controller = 
			(CorpusController)PersistenceManager.loadObjectFromFile(new File(stemmerFileUrl));

		corpus = new CorpusImpl();
		
		controller.setCorpus(corpus);
	}
	
	/**
	 * add stem counts to each SplitArticle in the provided collection
	 * @param splitArticleColl
	 * @throws ExecutionException
	 */
	@SuppressWarnings("unchecked")
	public void count(Collection<SplitArticle> splitArticleColl) throws ExecutionException {
		List<DocumentSplitArticlePair> docArtList = new ArrayList<>(batchSize);

		int i = 0;
		for (SplitArticle splitArticle : splitArticleColl) {
			final Document document = new DocumentImpl();
			document.setName(splitArticle.file.getName() + i);
			
			docArtList.add(new DocumentSplitArticlePair(document, splitArticle));
			
			final String content = splitArticle.convertBodyLinesToString();
			
			DocumentContentImpl docContent = new DocumentContentImpl(content);
			document.setContent(docContent);
			corpus.add(document);
			
			i++;
			
			if (i%batchSize == 0) {
				processBatch(docArtList);
				
				docArtList.clear();
				corpus.clear();

				System.out.println("stems calculated for:  " + i);
			}
		}
		
		if (i%batchSize != 0) {
			processBatch(docArtList);
			System.out.println("stems calculated for:  " + i);
		}
	}
	
	private void processBatch(List<DocumentSplitArticlePair> docArtList) throws ExecutionException {
		controller.execute();
		
		for (DocumentSplitArticlePair pair : docArtList) {
			pair.splitArticle.stemCountMap = convertCounterMap(calculateStemCount(pair.document));
		}
	}
	
	static Map<String, Integer> convertCounterMap(Map<String, Counter> stringCounterMap) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		
		for (String string : stringCounterMap.keySet()) {
			final int value = stringCounterMap.get(string).getCount();
			
			result.put(string, value);
		}
		
		return result;
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
	
	private class DocumentSplitArticlePair {
		final public Document document;
		final public SplitArticle splitArticle;

		public DocumentSplitArticlePair(Document document,
				SplitArticle splitArticle) {
			this.document = document;
			this.splitArticle = splitArticle;
		}
	}
}
