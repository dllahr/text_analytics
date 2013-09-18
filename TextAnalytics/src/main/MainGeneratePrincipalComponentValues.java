package main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import orm.Article;
import orm.Eigenvalue;
import orm.SessionManager;

import controller.prediction.principalComponent.ArticlePcValueSaver;
import controller.prediction.principalComponent.ArticlePrincipalComponentValueCalculator;
import controller.prediction.principalComponent.ArticlePrincipalComponentValues;
import controller.util.Utilities;


public class MainGeneratePrincipalComponentValues {
	private static final String dateFormatString = "yyyy-MM-dd";
	
	private static final String minDateOption = "-min_d";
	private static final String maxDateOption = "-max_d";
	private static final String printOption = "-print";
	private static final String noSaveOption = "-noSave";

	private static final String mostRecentDateQueryString = "select max(publish_date) from article where " +
			"scoring_model_id = :scoringModelId and id in (select article_id from article_pc_value)";
	
	public static void main(String[] args) throws ParseException {
		System.out.println("Generate principal component values");
		
		final int scoringModelId = Integer.valueOf(args[0]);
		System.out.println("scoring model id:  " + scoringModelId);
		
		final int articleSourceId = Integer.valueOf(args[1]);
		System.out.println("article source id:  " + articleSourceId);
		
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		
		Date minDate = null;
		Date maxDate = null;
		boolean doPrint = false;
		boolean doSave = true;
		for (int i = 2; i < args.length; i++) {
			if (args[i].equals(minDateOption)) {
				System.out.println(minDateOption + " option present, parsing min date from arguments");
				i++;
				
				minDate = dateFormat.parse(args[i]);
			} else if (args[i].equals(maxDateOption)) {
				i++;
				maxDate = dateFormat.parse(args[i]);
				System.out.println(maxDateOption + " option present, parsing max date from arguments: " + maxDate);
			} else if (args[i].equals(printOption)) {
				System.out.println(printOption + " present, will print out results");
				doPrint = true;
			} else if (args[i].equals(noSaveOption)) {
				System.out.println(noSaveOption + " present, will not save results to database");
				doSave = false;
			}
		}
		
		if (null == minDate) {
			minDate = getMostRecentDayIndexOfArticleWithPrincipalComponentValue(scoringModelId);
			System.out.println("minimum date is lastest date of articles with principal component values in database");
		}
		System.out.println("Min date:  " + minDate + " " + Utilities.calculateDayIndex(minDate));

		List<Integer> articleIdList = Article.getArticleIdsForMinDateAndArticleSource(minDate, maxDate, articleSourceId);
		
		List<ArticlePrincipalComponentValues> list = 
				(new ArticlePrincipalComponentValueCalculator()).calculate(scoringModelId, articleIdList);
		
		if (doPrint) {
			for (ArticlePrincipalComponentValues apcv : list) {
				print(apcv);
			}
		}
		
		if (doSave) {
			(new ArticlePcValueSaver()).save(list);
		}
	}
	
	static Date getMostRecentDayIndexOfArticleWithPrincipalComponentValue(int scoringModelId) {
		Query query = SessionManager.createSqlQuery(mostRecentDateQueryString);
		query.setInteger("scoringModelId", scoringModelId);
		
		return (Date)query.list().get(0);
	}
	

	static void print(ArticlePrincipalComponentValues apcv) {
		List<Eigenvalue> eigList = new ArrayList<>(apcv.prinCompValuesMap.keySet());
		
		Collections.sort(eigList, new Comparator<Eigenvalue>() {
			@Override
			public int compare(Eigenvalue o1, Eigenvalue o2) {
				return o1.getId() - o2.getId();
			}
		});
		
		for (Eigenvalue eig : eigList) {
			System.out.println(apcv.article.getId() + " " + eig.getId() + " " + apcv.prinCompValuesMap.get(eig));
		}
	}
}
