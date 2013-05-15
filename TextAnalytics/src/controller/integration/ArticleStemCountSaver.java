package controller.integration;

import java.util.List;

import orm.ScoringModel;

import controller.dateExtractionConversion.ArticleFileDatePair;
import controller.integration.readAndSplitRawFile.SplitArticle;
import controller.stemCountArticles.StemFrequencyDistribution;

public class ArticleStemCountSaver {

	public void save(List<SplitArticle> splitArticleList, ScoringModel scoringModel) {
		for (SplitArticle splitArticle : splitArticleList) {
			ArticleFileDatePair pair = new ArticleFileDatePair(splitArticle.file, splitArticle.articleDate);
			
			StemFrequencyDistribution.saveStemCountToDatabase(splitArticle.stemCountMap, pair, scoringModel);
		}
	}
}
