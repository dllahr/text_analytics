package main;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;










import orm.Article;
import orm.Constants;
import orm.PredictionModel;
import orm.RegressionModel;
import orm.RegressionModelCoef;
import orm.RegressionModelType;
import controller.prediction.regressionModel.DayIndexRawPredictionPair;
import controller.prediction.regressionModel.DayIndexRawPredictionPairBuilder;
import controller.prediction.regressionModel.DayPrincipalComponentValueVector;
import controller.prediction.regressionModel.DayPrincipalComponentValueVectorBuilder;
import controller.prediction.regressionModel.ExtremaPredictionBuilder;
import controller.prediction.regressionModel.ExtremaPredictionResultBuilder;
import controller.prediction.regressionModel.Prediction;
import controller.prediction.regressionModel.PredictionBuilder;
import controller.prediction.regressionModel.PredictionResultBuilder;
import controller.prediction.regressionModel.PricePredictionBuilder;
import controller.prediction.regressionModel.PricePredictionResultBuilder;
import controller.util.Utilities;


public class MainGeneratePredictions {
	
	
	public static void main(String[] args) throws ParseException {
		//parse command line options
		final int regressionModelId = Integer.valueOf(args[0]);
		
		final List<Integer> predictionModelIdList = parsePredictionModelIdListParameter(args[1]);
		
		final Date minArticleDate = Utilities.dateFormat.parse(args[2]);
		
		final int articleSourceId = Integer.valueOf(args[3]);
		
		final Date maxArticleDate = args.length >= 5 ? Utilities.dateFormat.parse(args[4]) : null;
		
		
		//commence da jigglin
		System.out.println("find regression model with ID:  " + regressionModelId);
		RegressionModel rm = RegressionModel.findById(regressionModelId);
		
		List<Integer> articleIdList = Article.getArticleIdsForMinDateAndArticleSource(minArticleDate, maxArticleDate,
				articleSourceId, false, false);
		System.out.println("found article ID's:  " + articleIdList.size());
		
		System.out.print("find prediction model with ID's:  ");
		for (int id : predictionModelIdList) {
			System.out.print(id + " ");
		}
		System.out.println();
		List<PredictionModel> pmList = PredictionModel.findAllById(predictionModelIdList);
		
		System.out.println("retrieve principal component values aggregated by article publish day.  Min/Max article dates: " + args[2] + " " + (args.length >= 5 ? args[4] : ""));
		List<DayPrincipalComponentValueVector> dayPcValVectList = 
				(new DayPrincipalComponentValueVectorBuilder()).build(articleIdList, 
						RegressionModelCoef.retrieveEigenvalueIdsForRegressionModel(regressionModelId));
		
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
		System.out.println("start date, end date, fraction change");
		for (DayIndexRawPredictionPair raw : rawPredictionList) {
			System.out.println(raw);
		}
		System.out.println("total count of raw predictions: " + rawPredictionList.size());
		System.out.println();


		System.out.println("generate threshold predictions:");
		PredictionBuilder predictionBuilder;
		if (rm.getModelType() == RegressionModelType.Price) {
			predictionBuilder = new PricePredictionBuilder();
		} else if (rm.getModelType() == RegressionModelType.Extrema) {
			predictionBuilder = new ExtremaPredictionBuilder();
		} else {
			throw new RuntimeException("MainGeneratePredictions main can only create predictionBuilder for "
					+ "regressionModelType Price or Extrema, this is not supported:  " + rm.getModelType());
		}
		List<Prediction> predictionList = predictionBuilder.build(pmList, rawPredictionList);
		
		if (predictionList.size() > 0) {
			System.out.println("compare predictions to actual results:");
			
			PredictionResultBuilder predictionResultBuilder;
			if (rm.getModelType() == RegressionModelType.Price) {
				predictionResultBuilder = new PricePredictionResultBuilder();
			} else if (rm.getModelType() == RegressionModelType.Extrema) {
				predictionResultBuilder = new ExtremaPredictionResultBuilder();
			} else {
				throw new RuntimeException("MainGeneratePredictions main can only create predictionResultBuilder for "
						+ "regressionModelType Price or Extrema, this is not supported:  " + rm.getModelType());
			}
			
			predictionResultBuilder.build(predictionList);
		} else {
			System.out.println("no predictions made, no comparison to attempted");
		}

		System.out.println("predictions and results where available:");
		System.out.println(Prediction.toStringHeader() + Constants.toStringDelimeter + "initialDate" 
			+ Constants.toStringDelimeter + "predictionDate");
		for (Prediction prediction : predictionList) {
			StringBuilder builder = new StringBuilder();
			builder.append(prediction).append(" ");
			builder.append(Utilities.dateFormat.format(Utilities.calculateDate(prediction.initialDayIndex))).append(" ");
			builder.append(Utilities.dateFormat.format(Utilities.calculateDate(prediction.predictionDayIndex))).append(" ");
			
			System.out.println(builder);
		}
		System.out.println();


		System.out.println("total count of raw predictions: " + rawPredictionList.size());
		System.out.println("total count thresholded predictions: " + predictionList.size());
		
		System.out.println();
		Map<PredictionModel, Integer[]> predictionModelResultBinMap = calculateResultBins(predictionList);
		for (PredictionModel pm : pmList) {
			System.out.println("fraction of results in each quartile, for prediction model " + pm.getId());

			Integer[] resultBins = predictionModelResultBinMap.get(pm);
			
			if (resultBins != null) {
				double[] fracResultBins = calculateFracResultBins(resultBins);
				for (double fraction : fracResultBins) {
					System.out.print(String.format("%.3G", fraction) + " ");
				}
				System.out.println();

			} else {
				System.out.println("Warning:  no predictions made for prediction model " + pm.getId());
			}
			
			System.out.println();
		}		
	}
	
	static double[] calculateFracResultBins(Integer[] resultBins) {
		double[] fracResultBins = new double[resultBins.length];
		
		int sum = 0;
		for (int bin : resultBins) {
			sum += bin;
		}
		System.out.println("total predictions with results:  " + sum);

		for (int i = 0; i < fracResultBins.length; i++) {
			fracResultBins[i] = ((double)resultBins[i])/((double)sum);
		}
		
		return fracResultBins;
	}
	
	static Map<PredictionModel, Integer[]> calculateResultBins(List<Prediction> predictionList) {
		Map<PredictionModel, Integer[]> result = new HashMap<PredictionModel, Integer[]>();
		
		for (Prediction prediction : predictionList) {
			Integer[] resultBins = result.get(prediction.predictionModel);
			if (null == resultBins) {
				final int numBins = 4;
				resultBins = new Integer[numBins];
				for (int i = 0; i < numBins; i++) {
					resultBins[i] = 0;
				}

				result.put(prediction.predictionModel, resultBins);
			}

			if (prediction.result != null) {
				if (prediction.result <= prediction.percentile25) {
					resultBins[0]++;
				} else if (prediction.result <= prediction.percentile50) {
					resultBins[1]++;
				} else if (prediction.result <= prediction.percentile75) {
					resultBins[2]++;
				} else {
					resultBins[3]++;
				}
			}
		}
		
		return result;
	}
	
	static List<Integer> parsePredictionModelIdListParameter(String predictionModelIdListParam) {
		String[] split = predictionModelIdListParam.trim().split(",");
		
		List<Integer> result = new ArrayList<>(split.length);
		
		for (int i = 0; i < split.length; i++) {
			result.add(Integer.valueOf(split[i].trim()));
		}
		
		return result;
	}
}
