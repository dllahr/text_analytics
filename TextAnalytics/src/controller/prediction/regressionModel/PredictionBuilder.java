package controller.prediction.regressionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import controller.stockPrices.SmoothedStockPrices;

import orm.PredictionModel;
import orm.PredictionModelStockSmoothingCoef;

public class PredictionBuilder {

	/**
	 * @param pm
	 * @param rawPredColl sorted by dayIndex
	 * @return
	 */
	public List<Prediction> build(PredictionModel pm, List<DayIndexRawPredictionPair> rawPredColl) {

		List<Prediction> result;
		
		List<DayIndexRawPredictionPair> filteredRawList = filterRawPredictions(rawPredColl, pm);
		
		if (filteredRawList.size() > 0) {
			final int minDayIndex = filteredRawList.get(0).initialDayIndex;
			
			SmoothedStockPriceParams params = determineParams(pm.buildRelDayIndexSortedList(), minDayIndex);
			
			SmoothedStockPrices smoothedStockPrices = new SmoothedStockPrices(params.minDayIndex, 
					pm.getRegressionModel().getCompany(), params.weights);
			
			result = buildPredictions(smoothedStockPrices, filteredRawList, pm);
		} else {
			System.out.println("PredictionBuilder build none of the raw predictions passed the filter for prediction model:  " + pm.getId());
			
			result = new LinkedList<>();
		}
		
		return result;
	}

	static List<Prediction> buildPredictions(SmoothedStockPrices smoothedStockPrices, 
			List<DayIndexRawPredictionPair> rawList, PredictionModel pm) {

		List<Prediction> result = new ArrayList<>(rawList.size());
		
		for (DayIndexRawPredictionPair raw : rawList) {
			if (raw.initialDayIndex <= smoothedStockPrices.getMaxDayIndex()) {
				final double initialPrice = smoothedStockPrices.getSmoothedPriceClosestAfterDayIndex(raw.initialDayIndex);
				final double pricePercentile25 = pm.getPercentile25Value() * initialPrice;
				final double pricePercentile50 = pm.getPercentile50Value() * initialPrice;
				final double pricePercentile75 = pm.getPercentile75Value() * initialPrice;

				result.add(new Prediction(raw.initialDayIndex, raw.predictionDayIndex, pricePercentile25, pricePercentile50, pricePercentile75));
			} else {
				System.out.println("PredictionBuilder buildPredictions unable to calculate prediction because stock data not in database.  day index:  " 
						+ raw.initialDayIndex);
			}
		}
		
		return result;
	}

	public static SmoothedStockPriceParams determineParams(List<PredictionModelStockSmoothingCoef> coefList, 
			int minPredictionDayIndex) {

		final int minRelativeDayIndex = coefList.get(0).getRelativeDayIndex();
		final int maxRelativeDayIndex = coefList.get(coefList.size() - 1).getRelativeDayIndex();
		final int numDays = maxRelativeDayIndex - minRelativeDayIndex + 1;

		final double[] weights = new double[numDays];

		for (PredictionModelStockSmoothingCoef coef : coefList) {
			final int index = coef.getRelativeDayIndex() - minRelativeDayIndex;
			weights[index] = coef.getCoef();
		}

		int minDayIndex = minPredictionDayIndex + minRelativeDayIndex - 7;
		
		return new SmoothedStockPriceParams(weights, minDayIndex);
	}

	static List<DayIndexRawPredictionPair> filterRawPredictions(Collection<DayIndexRawPredictionPair> rawPredColl, 
			PredictionModel pm) {

		PredictionModel.Filter filter = pm.buildFilter();
		
		List<DayIndexRawPredictionPair> result = new LinkedList<>();
		
		for (DayIndexRawPredictionPair raw : rawPredColl) {
			if (filter.passesFilter(raw.prediction)) {
				result.add(raw);
			}
		}
		
		return result;
	}
	
	static class SmoothedStockPriceParams {
		final double[] weights;
		final int minDayIndex;

		public SmoothedStockPriceParams(double[] weights, int minDayIndex) {
			this.weights = weights;
			this.minDayIndex = minDayIndex;
		}
	}
}
