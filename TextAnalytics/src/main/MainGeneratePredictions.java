package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import orm.Article;
import orm.PredictionModel;
import orm.RegressionModel;

import controller.prediction.regressionModel.DayIndexRawPredictionPair;
import controller.prediction.regressionModel.DayIndexRawPredictionPairBuilder;
import controller.prediction.regressionModel.DayPrincipalComponentValueVector;
import controller.prediction.regressionModel.DayPrincipalComponentValueVectorBuilder;
import controller.prediction.regressionModel.Prediction;
import controller.prediction.regressionModel.PredictionBuilder;
import controller.prediction.regressionModel.PredictionResultBuilder;


public class MainGeneratePredictions {
	private static final String dateFormatString = "yyyy-MM-dd";
	
	public static void main(String[] args) throws ParseException {
		final int regressionModelId = Integer.valueOf(args[0]);
		final Date minArticleDate = (new SimpleDateFormat(dateFormatString)).parse(args[1]);
		final int predictionModelId = Integer.valueOf(args[2]);
		
		final int minArticleDayIndex = Article.calculateDayIndex(minArticleDate);
		
		System.out.println("find regression model with ID:  " + regressionModelId);
		RegressionModel rm = RegressionModel.findById(regressionModelId);
		
		System.out.println("find prediction model with ID:  " + predictionModelId);
		PredictionModel pm = PredictionModel.findById(predictionModelId);
		
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
		
		System.out.println("lookup results of predictions:");
		(new PredictionResultBuilder()).build(predictionList, pm);
		
		int[] resultBins = {0, 0, 0, 0};
		
		System.out.println("predictions and results where available:");
		for (Prediction prediction : predictionList) {
			System.out.println(prediction);
			
			if (prediction.result != null) {
				if (prediction.result <= prediction.pricePercentile25) {
					resultBins[0]++;
				} else if (prediction.result <= prediction.pricePercentile50) {
					resultBins[1]++;
				} else if (prediction.result <= prediction.pricePercentile75) {
					resultBins[2]++;
				} else {
					resultBins[3]++;
				}
			}
		}
		
		System.out.println("fraction of results in each quartile:");
		double[] fracResultBins = calculateFracResultBins(resultBins);
		for (double fraction : fracResultBins) {
			System.out.print(fraction + " ");
		}
		System.out.println();
	}
	
	static double[] calculateFracResultBins(int[] resultBins) {
		double[] fracResultBins = new double[resultBins.length];
		
		int sum = 0;
		for (int bin : resultBins) {
			sum += bin;
		}

		for (int i = 0; i < fracResultBins.length; i++) {
			fracResultBins[i] = ((double)resultBins[i])/((double)sum);
		}
		
		return fracResultBins;
	}
}
