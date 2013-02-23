package controller.predictFromArticles;

import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import controller.dateExtractionConversion.ArticleFileDatePair;
import controller.dateExtractionConversion.ReadDateFromArticle;

import orm.Article;
import orm.Company;
import orm.SessionManager;

public class LoadAndScoreArticles extends Thread {

	private File individualArticleDir;
	
	private Company company;
	
	public LoadAndScoreArticles() {
		
	}
	
	public File getIndividualArticleDir() {
		return individualArticleDir;
	}


	public void setIndividualArticleDir(File individualArticleDir) {
		this.individualArticleDir = individualArticleDir;
	}


	public Company getCompany() {
		return company;
	}


	public void setCompany(Company company) {
		this.company = company;
	}

	public void loadAndScore() {
		List<ArticleFileDatePair> articleFileDatePairList = getArticleFilesWithDates(individualArticleDir);
		if (articleFileDatePairList.size() > 0) {
			try {
				List<Article> articleList = StemFrequencyDistribution.calculateAndSaveArticleStems(articleFileDatePairList, company);
//				CalcPrincipalComponentValues.calcPrincipalComponentValuesForArticles(articleList);
				GeneratePredictions.generatePredictions(articleList);
				SessionManager.commit();
			} catch (GateException | IOException e) {
				System.err.println("PredictFromArticles start() GateException");
				e.printStackTrace();
			}
		}
	}
	
	private static List<ArticleFileDatePair> getArticleFilesWithDates(File individualArticleDir) {
		File[] articleFileArray = individualArticleDir.listFiles();
		List<ArticleFileDatePair> result = new ArrayList<>(articleFileArray.length);
		for (File curFile : articleFileArray) {
			if (! curFile.isDirectory()) {
				Date date = ReadDateFromArticle.readDate(curFile, ReadDateFromArticle.DateLineStyle.newStyle);
				if (date != null) {
					result.add(new ArticleFileDatePair(curFile, date));
				} else {
					System.err.println("Unable to read date from " + curFile.getAbsolutePath());
				}
			}
		}
		
		return result;
	}
	

	public void loadWithoutDate() {
		List<ArticleFileDatePair> articleFileDatePairList = getArticleFilesWithoutDate(individualArticleDir);
		if (articleFileDatePairList.size() > 0) {
			try {
				StemFrequencyDistribution.calculateAndSaveArticleStems(articleFileDatePairList, company);
				SessionManager.commit();
			} catch (GateException | IOException e) {
				System.err.println("PredictFromArticles start() GateException");
				e.printStackTrace();
			}
		}
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
