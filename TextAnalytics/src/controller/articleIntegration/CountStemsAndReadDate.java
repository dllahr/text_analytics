package controller.articleIntegration;

import gate.creole.ExecutionException;
import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controller.articleIntegration.readAndSplitRawFile.ArticleSectionSplitter;
import controller.articleIntegration.readAndSplitRawFile.MultiplelArticleSplitter;
import controller.articleIntegration.readAndSplitRawFile.RawArticle;
import controller.articleIntegration.readAndSplitRawFile.SplitArticle;
import controller.dateExtractionConversion.DateExtractor;
import controller.stemCountArticles.StemCounter;

public class CountStemsAndReadDate {
	
	private final MultiplelArticleSplitter multiplelArticleSplitter;
	
	private final ArticleSectionSplitter articleSectionSplitter;
	
	private final StemCounter stemCounter;
	
	private final List<DateExtractor> dateExtractorList;
	
	/**
	 * @param labelIsBodyMap map indicating which of the labels which the file indicate the body of the article
	 * @param dateExtractor to be used to identify and parse dates from the file
	 * @param articleStemmingBatchSize  number of article to do as a batch
	 * @throws GateException
	 * @throws IOException
	 */
	public CountStemsAndReadDate(Map<String, Boolean> labelIsBodyMap, List<DateExtractor> dateExtractorList, 
			int articleStemmingBatchSize) throws GateException, IOException {
		
		multiplelArticleSplitter = new MultiplelArticleSplitter();

		articleSectionSplitter = new ArticleSectionSplitter(labelIsBodyMap);
		
		stemCounter = new StemCounter(articleStemmingBatchSize);
		
		this.dateExtractorList = Collections.unmodifiableList(new ArrayList<>(dateExtractorList));
	}
	
	public List<SplitArticle> doAll(Collection<File> fileColl) throws IOException, ExecutionException {
		List<SplitArticle> splitArticleList = new LinkedList<>();
		
		for (File file : fileColl) {
			System.out.println("reading from file " + file.getName());

			for (RawArticle rawArticle : multiplelArticleSplitter.readAndParse(file)) {
				SplitArticle splitArticle = articleSectionSplitter.split(rawArticle);
				
				if (splitArticle.bodyLines.size() > 0) {
					splitArticleList.add(splitArticle);
				}
			}
		}

		stemCounter.count(splitArticleList);

		System.out.println("reading date from articles");
		for (SplitArticle splitArticle : splitArticleList) {
			
			splitArticle.articleDate = null;
			Iterator<DateExtractor> dateExtractorIterator = dateExtractorList.iterator();
			while (null == splitArticle.articleDate && dateExtractorIterator.hasNext()) {
				DateExtractor dateExtractor = dateExtractorIterator.next();
				
				splitArticle.articleDate = dateExtractor.extract(splitArticle.linesAfterBody);
			}
			
			if (null == splitArticle.articleDate) {
				System.out.println("warning: failed to parse date from article " + splitArticle.file.getName() + " " + splitArticle.linesBeforeBody.get(0));
			}
		}
		
		return splitArticleList;
	}
}
