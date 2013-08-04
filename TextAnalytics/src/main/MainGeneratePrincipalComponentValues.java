package main;

import java.math.BigDecimal;
import java.text.ParseException;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import orm.Constants;
import orm.SessionManager;

import controller.prediction.principalComponent.ArticlePcValueSaver;
import controller.prediction.principalComponent.ArticlePrincipalComponentValueCalculator;
import controller.prediction.principalComponent.ArticlePrincipalComponentValues;


public class MainGeneratePrincipalComponentValues {

	private static final String mostRecentDateQueryString = "select max(day_index) from article where " +
			"scoring_model_id = :scoringModelId and id in (select article_id from article_pc_value)";
	
	public static void main(String[] args) throws ParseException {
		final int scoringModelId = Integer.valueOf(args[0]);
		
		final int mostRecentDayIndex = getMostRecentDayIndexOfArticleWithPrincipalComponentValue(scoringModelId);
		
		final Date minDate = new Date(Constants.millisPerDay*(mostRecentDayIndex+1));
		
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
