package controller.integration;

import gate.creole.ExecutionException;
import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controller.dateExtractionConversion.ReadDateFromArticle;
import controller.dateExtractionConversion.ReadDateFromArticle.DateLineStyle;
import controller.integration.readAndSplitRawFile.ArticleSectionSplitter;
import controller.integration.readAndSplitRawFile.MultiplelArticleSplitter;
import controller.integration.readAndSplitRawFile.RawArticle;
import controller.integration.readAndSplitRawFile.SplitArticle;
import controller.stemCountArticles.StemCounter;

public class CountStemsAndReadDate {
	
	private final MultiplelArticleSplitter multiplelArticleSplitter;
	
	private final ArticleSectionSplitter articleSectionSplitter;
	
	private final StemCounter stemCounter;
	
	private final List<DateLineStyle> dateLineStyleList;
	
	public CountStemsAndReadDate(Map<String, Boolean> labelIsBodyMap) throws GateException, IOException {
		multiplelArticleSplitter = new MultiplelArticleSplitter();

		articleSectionSplitter = new ArticleSectionSplitter(labelIsBodyMap);
		
		stemCounter = new StemCounter();
		
		dateLineStyleList = new LinkedList<>();
		dateLineStyleList.add(DateLineStyle.newStyle);
		dateLineStyleList.add(DateLineStyle.original);
	}
	
	public List<SplitArticle> doAll(Collection<File> fileColl) throws IOException, ExecutionException {
		List<SplitArticle> splitArticleList = new LinkedList<>();
		
		for (File file : fileColl) {
			for (RawArticle rawArticle : multiplelArticleSplitter.readAndParse(file)) {
				splitArticleList.add(articleSectionSplitter.split(rawArticle));
			}
		}

		stemCounter.count(splitArticleList);

		for (SplitArticle splitArticle : splitArticleList) {
			Iterator<DateLineStyle> styleIter = dateLineStyleList.iterator();
			Date articleDate = null;
			while (styleIter.hasNext() && null == articleDate) {
				articleDate = ReadDateFromArticle.readDate(splitArticle.linesAfterBody, styleIter.next());
			}
			
			splitArticle.articleDate = articleDate;
		}
		
		return splitArticleList;
	}
}
