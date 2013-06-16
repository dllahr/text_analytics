package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import orm.Article;
import orm.PredictionModel;
import orm.RegressionModel;
import orm.SessionManager;

import controller.prediction.regressionModel.DayIndexRawPredictionPair;
import controller.prediction.regressionModel.DayIndexRawPredictionPairBuilder;
import controller.prediction.regressionModel.DayPrincipalComponentValueVector;
import controller.prediction.regressionModel.DayPrincipalComponentValueVectorBuilder;
import controller.prediction.regressionModel.Prediction;
import controller.prediction.regressionModel.PredictionBuilder;


public class MainGeneratePredictions {
	private static final String dateFormatString = "yyyy-MM-dd";
	
	public static void main(String[] args) throws ParseException {
		final int regressionModelId = Integer.valueOf(args[0]);
		final Date minArticleDate = (new SimpleDateFormat(dateFormatString)).parse(args[1]);
		final int predictionModelId = Integer.valueOf(args[2]);
		
		final int minArticleDayIndex = Article.calculateDayIndex(minArticleDate);
		
		System.out.println("find regression model with ID:  " + regressionModelId);
		RegressionModel rm = findRegressionModel(regressionModelId);
		
		System.out.println("find prediction model with ID:  " + predictionModelId);
		PredictionModel pm = findPredictionModel(predictionModelId);
		
		System.out.println("retrieve principal component values aggregated by article publish day.  min article date:  " + minArticleDayIndex);
		List<DayPrincipalComponentValueVector> dayPcValVectList = 
				(new DayPrincipalComponentValueVectorBuilder()).build(rm.getScoringModel().getId(), minArticleDayIndex);
		
		System.out.println("generate raw predictions based regression model and principal component values:");
		List<DayIndexRawPredictionPair> rawPredictionList = 
				(new DayIndexRawPredictionPairBuilder()).build(rm, dayPcValVectList);
		
		System.out.println("sort raw predictions by date:");
		Collections.sort(rawPredictionList, new Comparator<DayIndexRawPredictionPair>() {
			@Override
			public int compare(DayIndexRawPredictionPair o1, DayIndexRawPredictionPair o2) {
				return o1.initialDayIndex - o2.initialDayIndex;
			}
		});
		
		System.out.println("raw predictions:");
		for (DayIndexRawPredictionPair raw : rawPredictionList) {
			System.out.println(raw);
		}
		
		System.out.println("generate predictions:");
		List<Prediction> predictionList = (new PredictionBuilder()).build(pm, rawPredictionList);
		
		System.out.println("predictions:");
		for (Prediction prediction : predictionList) {
			System.out.println(prediction);
		}
	}
	
	static RegressionModel findRegressionModel(int regressionModelId) {
		Query query = SessionManager.createQuery("from RegressionModel where id = :id");
		query.setInteger("id", regressionModelId);
		
		return (RegressionModel)query.list().get(0);
	}
	
	static PredictionModel findPredictionModel(int predictionModelId) {
		Query query = SessionManager.createQuery("from PredictionModel where id = :id");
		query.setInteger("id", predictionModelId);
		
		return (PredictionModel)query.list().get(0);
	}
}
