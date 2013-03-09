package controller.legacy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import orm.Article;
import orm.ScoringModel;
import orm.SessionManager;

import controller.dateExtractionConversion.ArticleFileDatePair;
import controller.dateExtractionConversion.ReadDateFromArticle;
import controller.util.Utilities;

public class LoadArticles {
	private static final long millisPerDay = 24*60*60*1000;
	
	private static final int progressIncrement = 1000;
	
	public static void load(File articleDir, String articlePrefix, File articleOrderFile, ScoringModel company) {
		final List<String> articleFilenameOrderList;
		try {
			 articleFilenameOrderList = loadArticleOrder(articleOrderFile);
		} catch (IOException e) {
			System.err.println("LoadArticles load unable to read articleOrderFile - file containing order of articles");
			e.printStackTrace();
			return;
		}
		
		Map<String, ArticleFileDatePair> articleDateMap = loadAndConvertArticleDates(articleDir, articlePrefix);
		
		int articleId = Utilities.getMaxId("Article") + 1;
		
		for (String articleFileName : articleFilenameOrderList) {
			Article article = new Article();
			article.setId(articleId);
			articleId++;
			
			article.setCompany(company);
			
			ArticleFileDatePair pair = articleDateMap.get(articleFileName);
			article.setFilename(pair.getFile().getAbsolutePath());
			
			if (pair.getDate() != null) {
				article.setPublishDate(pair.getDate());
				article.setDayIndex((int)(pair.getDate().getTime() / millisPerDay));
			}
			
			SessionManager.persist(article);
		}
		
		SessionManager.commit();
	}

	
	private static Map<String, ArticleFileDatePair> loadAndConvertArticleDates(File articleDir, final String articlePrefix) {
		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return (!pathname.isDirectory()) && pathname.getName().startsWith(articlePrefix);
			}
		};
		
		Map<String, ArticleFileDatePair> result = new HashMap<String, ArticleFileDatePair>();
		
		ReadDateFromArticle.setShouldDisplayProgress(false);
		
		int progress = 0;
		for (File articleFile : articleDir.listFiles(fileFilter)) {
			Date articleDate = ReadDateFromArticle.readDate(articleFile, ReadDateFromArticle.DateLineStyle.original);
			result.put(articleFile.getName(), new ArticleFileDatePair(articleFile, articleDate));
			
			if (null == articleDate) {
				System.err.println("Could not read date from article:  " + articleFile.getName());
			}
			
			progress++;
			if ((progress % progressIncrement) == 0) {
				System.out.println("article dates attempted to analyze:  " + progress);
			}
		}
		
		return result;
	}


	private static List<String> loadArticleOrder(File articleOrderFile) throws IOException {
		List<String> result = new LinkedList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(articleOrderFile));
		
		String curLine;
		while ((curLine = reader.readLine()) != null) {
			result.add(curLine);
		}
		
		reader.close();
		
		return result;
	}
}
