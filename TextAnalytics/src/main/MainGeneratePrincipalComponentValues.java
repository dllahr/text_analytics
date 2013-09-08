package main;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import orm.SessionManager;

import controller.prediction.principalComponent.ArticlePcValueSaver;
import controller.prediction.principalComponent.ArticlePrincipalComponentValueCalculator;
import controller.prediction.principalComponent.ArticlePrincipalComponentValues;
import controller.util.Utilities;


public class MainGeneratePrincipalComponentValues {
	private static final String dateFormatString = "yyyy-MM-dd";

	private static final String mostRecentDateQueryString = "select max(day_index) from article where " +
			"scoring_model_id = :scoringModelId and id in (select article_id from article_pc_value)";
	
	public static void main(String[] args) throws ParseException {
		System.out.println("Generate principal component values");
		
		final int scoringModelId = Integer.valueOf(args[0]);
		System.out.println("scoring model id:  " + scoringModelId);
		
		final int mostRecentDayIndex;
		if (args.length > 1 && args[1].equals("-d")) {
			System.out.println("-d option present, parsing date from arguments");

			mostRecentDayIndex = Utilities.calculateDayIndex((new SimpleDateFormat(dateFormatString)).parse(args[2]));
		} else {
			mostRecentDayIndex = getMostRecentDayIndexOfArticleWithPrincipalComponentValue(scoringModelId);
		}
		System.out.println("Most recent date:  " + mostRecentDayIndex + " " + Utilities.calculateDate(mostRecentDayIndex));
		
		final int minDayIndex = mostRecentDayIndex+1;
		final Date minDate = Utilities.calculateDate(minDayIndex);
		System.out.println("start date:  " + minDayIndex + " " + minDate);
		
		List<ArticlePrincipalComponentValues> list = 
				(new ArticlePrincipalComponentValueCalculator()).calculate(scoringModelId, minDate);
		
		(new ArticlePcValueSaver()).save(list);
	}
	
	static int getMostRecentDayIndexOfArticleWithPrincipalComponentValue(int scoringModelId) {
		Query query = SessionManager.createSqlQuery(mostRecentDateQueryString);
		query.setInteger("scoringModelId", scoringModelId);
		
		return ((BigDecimal)query.list().get(0)).intValueExact();
	}
}
