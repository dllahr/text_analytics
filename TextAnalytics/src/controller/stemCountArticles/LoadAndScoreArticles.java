package controller.stemCountArticles;

import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import controller.dateExtractionConversion.ArticleFileDatePair;

import orm.ScoringModel;
import orm.SessionManager;

public class LoadAndScoreArticles extends Thread {

	private File individualArticleDir;
	
	private ScoringModel scoringModel;
	
	public LoadAndScoreArticles() {
		
	}
	
	public File getIndividualArticleDir() {
		return individualArticleDir;
	}


	public void setIndividualArticleDir(File individualArticleDir) {
		this.individualArticleDir = individualArticleDir;
	}


	public ScoringModel getScoringModel() {
		return scoringModel;
	}


	public void setScoringModel(ScoringModel company) {
		this.scoringModel = company;
	}

	

	public void loadWithoutDate() {
		Date startDate = new Date();
		System.out.println("loadWithoutDate start " + startDate);
		
		List<ArticleFileDatePair> articleFileDatePairList = getArticleFilesWithoutDate(individualArticleDir);
		if (articleFileDatePairList.size() > 0) {
			try {
				StemFrequencyDistribution.calculateAndSaveArticleStems(articleFileDatePairList, scoringModel);
				SessionManager.commit();
			} catch (GateException | IOException e) {
				System.err.println("PredictFromArticles start() GateException");
				e.printStackTrace();
			}
		}
		
		Date endDate = new Date();
		double duration = (double)(endDate.getTime() - startDate.getTime()) / 60000.0;
		System.out.println("loadWithoutDate end " + endDate + " duration[min] " + duration);
	}
	
	private static List<ArticleFileDatePair> getArticleFilesWithoutDate(File individualArticleDir) {
		File[] articleFileArray = individualArticleDir.listFiles();
		List<ArticleFileDatePair> result = new ArrayList<>(articleFileArray.length);
		for (File curFile : articleFileArray) {
			if (! curFile.isDirectory()) {
				result.add(new ArticleFileDatePair(curFile, null));
			}
		}
		
		return result;
	}
}
