package controller.integration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controller.integration.readAndSplitRawFile.ArticleSectionSplitter;
import controller.integration.readAndSplitRawFile.MultiplelArticleSplitter;
import controller.integration.readAndSplitRawFile.RawArticle;
import controller.integration.readAndSplitRawFile.SplitArticle;

public class ReadDateStemAndStore {
	
	private final MultiplelArticleSplitter multiplelArticleSplitter;
	
	private final ArticleSectionSplitter articleSectionSplitter;
	
	public ReadDateStemAndStore(Map<String, Boolean> labelIsBodyMap) {
		multiplelArticleSplitter = new MultiplelArticleSplitter();

		articleSectionSplitter = new ArticleSectionSplitter(labelIsBodyMap);
	}
	
	public void doAll(Collection<File> fileColl) throws IOException {
		List<SplitArticle> splitArticleList = new LinkedList<>();
		
		for (File file : fileColl) {
			for (RawArticle rawArticle : multiplelArticleSplitter.readAndParse(file)) {
				splitArticleList.add(articleSectionSplitter.split(rawArticle));
			}
		}
		
		for (SplitArticle splitArticle : splitArticleList) {
			//extract date
			
			//stem
		}
	}
}
