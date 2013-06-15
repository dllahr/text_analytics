package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import orm.Article;
import orm.RegressionModel;
import orm.SessionManager;

import controller.prediction.regressionModel.DayIndexPredictionPair;
import controller.prediction.regressionModel.DayIndexPredictionPairBuilder;
import controller.prediction.regressionModel.DayPrincipalComponentValueVector;
import controller.prediction.regressionModel.DayPrincipalComponentValueVectorBuilder;


public class MainGeneratePredictions {
	private static final String dateFormatString = "yyyy-MM-dd";
	
	public static void main(String[] args) throws ParseException {
		final int regressionModelId = Integer.valueOf(args[0]);
		final Date minArticleDate = (new SimpleDateFormat(dateFormatString)).parse(args[1]);
		final int minArticleDayIndex = Article.calculateDayIndex(minArticleDate);
		
		System.out.println("find regression model with ID:  " + regressionModelId);
		RegressionModel rm = findRegressionModel(regressionModelId);
		
		System.out.println("retrieve principal component values aggregated by article publish day.  min article date:  " + minArticleDayIndex);
		List<DayPrincipalComponentValueVector> dayPcValVectList = 
				(new DayPrincipalComponentValueVectorBuilder()).build(rm.getScoringModel().getId(), minArticleDayIndex);
		
		System.out.println("generate raw predictions based regression model and principal component values:");
		List<DayIndexPredictionPair> dayIndexPredictionPairList = 
				(new DayIndexPredictionPairBuilder()).build(rm, dayPcValVectList);
		
		System.out.println("sort predictions by date:");
		Collections.sort(dayIndexPredictionPairList, new Comparator<DayIndexPredictionPair>() {
			@Override
			public int compare(DayIndexPredictionPair o1, DayIndexPredictionPair o2) {
				return o1.initialDayIndex - o2.initialDayIndex;
			}
		});
		
		System.out.println("results:");
		for (DayIndexPredictionPair pair : dayIndexPredictionPairList) {
			System.out.println(pair.initialDayIndex + " " + pair.predictionDayIndex + " " + pair.prediction);
		}
		
	}
	
	static RegressionModel findRegressionModel(int regressionModelId) {
		Query query = SessionManager.createQuery("from RegressionModel where id = :id");
		query.setInteger("id", regressionModelId);
		
		return (RegressionModel)query.list().get(0);
	}
}
