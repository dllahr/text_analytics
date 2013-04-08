package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import orm.Article;
import orm.ScoringModel;
import orm.SessionManager;

import controller.dateExtractionConversion.ReadDateFromArticle;
import controller.dateExtractionConversion.ReadDateFromArticle.DateLineStyle;
import controller.util.Utilities;

public class MainExtractAndLoadDate {
	
	private static final String scoringModelIdParam = "scoringModelId";
	private static final String scoringModelQuery = "from ScoringModel where id =:" + scoringModelIdParam;
	
	private static final String scoringModelParam = "scoringModel";
	private static final String filenameParam = "filename";
	private static final String articleQueryString = "from Article where scoringModel =:" + scoringModelParam 
			+ " and filename like :" + filenameParam;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int scoringModelId = Integer.valueOf(args[0]);
		final String articleDirPath = args[1];
		
		System.out.println("Will attempt to read dates from articles in directory:  " + articleDirPath);
		File articleDir = new File(articleDirPath);
		
		ScoringModel scoringModel = lookupScoringModel(scoringModelId);
		System.out.println("Will attempt to apply dates to articles loaded against scoring model:  " + scoringModel);
		
		List<DateLineStyle> styleList = new ArrayList<>();
		styleList.add(DateLineStyle.original);
		styleList.add(DateLineStyle.newStyle);
		
		Query articleQuery = SessionManager.createQuery(articleQueryString);
		articleQuery.setParameter(scoringModelParam, scoringModel);
		
		ReadDateFromArticle.setShouldDisplayProgress(false);
		
		for (File articleFile : articleDir.listFiles()) {
			Iterator<DateLineStyle> styleIter = styleList.iterator();
			Date articleDate = null;
			while (null == articleDate && styleIter.hasNext()) {
				articleDate = ReadDateFromArticle.readDate(articleFile, styleIter.next());
			}
			
			if (articleDate != null) {
				articleQuery.setString(filenameParam, "%" + articleFile.getName());
				List<Article> articleList = Utilities.convertGenericList(articleQuery.list());
				if (articleList.size() == 1) {
					Article article = articleList.get(0);
					article.setPublishDate(articleDate);
					article.setDayIndex(articleDate);
					SessionManager.merge(article);
				} else if (articleList.size() == 0) {
					System.err.println("No article found in database that matches:  " + articleFile.getName());
				} else { //more than one article found
					System.err.println("More than one article found in database that matches:  " + articleFile.getName());
				}
			} else {
				System.err.println("Unable to extract date for:  " + articleFile.getName());
			}
			
		}
		
		SessionManager.commit();
	}

	private static ScoringModel lookupScoringModel(int scoringModelId) {
		Query query = SessionManager.createQuery(scoringModelQuery);
		query.setInteger(scoringModelIdParam, scoringModelId);
		
		return (ScoringModel)query.list().get(0);
	}
}
