package controller.predictFromArticles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.integration.readAndSplitRawFile.SplitArticle;

import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.corpora.CorpusImpl;
import gate.corpora.DocumentContentImpl;
import gate.corpora.DocumentImpl;
import gate.creole.ExecutionException;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

public class StemCounter {
	private final Corpus corpus;
	
	private final CorpusController controller;
	
	public StemCounter() throws GateException, IOException {
		System.out.println("gate installation directory: " + StemFrequencyDistribution.gateHome);
		if (gate.Gate.getGateHome() == null) {
			gate.Gate.setGateHome(new File(StemFrequencyDistribution.gateHome));
		}
		if (! gate.Gate.isInitialised()) {
			gate.Gate.init();
		}
		
		System.out.println("gate stemmer (JAPE):  " + StemFrequencyDistribution.stemmerFileUrl);
		controller = 
			(CorpusController)PersistenceManager.loadObjectFromFile(new File(StemFrequencyDistribution.stemmerFileUrl));

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
		List<DocumentSplitArticlePair> docArtList = new ArrayList<>(splitArticleColl.size());
		
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
		}
		
		controller.execute();
		
		for (DocumentSplitArticlePair pair : docArtList) {
			pair.splitArticle.stemCountMap = convertCounterMap(StemFrequencyDistribution.calculateStemCount(pair.document));
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
