package controller.prediction.regressionModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import controller.prediction.regressionModel.PredictionBuilder.SmoothedStockPriceParams;
import controller.stockPrices.SmoothedStockPrices;

import orm.PredictionModel;

public class PredictionResultBuilder {

	public void build(List<Prediction> predictionList, PredictionModel pm) {
		Prediction minDayIndexPrediction = Collections.min(predictionList, new Comparator<Prediction>() {
			@Override
			public int compare(Prediction arg0, Prediction arg1) {
				return arg0.predictionDayIndex - arg1.predictionDayIndex;
			}
		});

		SmoothedStockPriceParams params = PredictionBuilder.determineParams(pm.buildRelDayIndexSortedList(),
				minDayIndexPrediction.predictionDayIndex);
		
		SmoothedStockPrices smoothedStockPrices = new SmoothedStockPrices(params.minDayIndex, 
				pm.getRegressionModel().getCompany(), params.weights);
		
		for (Prediction prediction : predictionList) {
			if (prediction.predictionDayIndex <= smoothedStockPrices.getMaxDayIndex()) {
				prediction.result = smoothedStockPrices.getSmoothedPriceClosestAfterDayIndex(prediction.predictionDayIndex);
			}
		}
	}
}
