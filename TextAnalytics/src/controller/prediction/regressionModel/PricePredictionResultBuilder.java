package controller.prediction.regressionModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.prediction.regressionModel.PricePredictionBuilder.SmoothedStockPriceParams;
import controller.stockPrices.SmoothedStockPrices;

import orm.PredictionModel;

public class PricePredictionResultBuilder implements PredictionResultBuilder {

	/* (non-Javadoc)
	 * @see controller.prediction.regressionModel.PredictionResultsBuilder#build(java.util.List)
	 */
	@Override
	public void build(List<Prediction> predictionList) {
		
		Map<PredictionModel, SmoothedStockPrices> modelPricesMap = buildModelPricesMap(predictionList);

		for (Prediction prediction : predictionList) {
			SmoothedStockPrices smoothedStockPrices = modelPricesMap.get(prediction.predictionModel);
			
			if (prediction.predictionDayIndex <= smoothedStockPrices.getMaxDayIndex()) {
				prediction.result = smoothedStockPrices.getSmoothedPriceClosestAfterDayIndex(prediction.predictionDayIndex);
			}
		}
	}
	
	static Map<PredictionModel, SmoothedStockPrices> buildModelPricesMap(List<Prediction> predictionList) {
		Map<PredictionModel, SmoothedStockPrices> result = new HashMap<PredictionModel, SmoothedStockPrices>();
		
		Prediction minDayIndexPrediction = Collections.min(predictionList, new Comparator<Prediction>() {
			@Override
			public int compare(Prediction arg0, Prediction arg1) {
				return arg0.predictionDayIndex - arg1.predictionDayIndex;
			}
		});
		
		for (Prediction prediction : predictionList) {
			PredictionModel pm = prediction.predictionModel;
			
			if (! result.containsKey(pm)) {
				SmoothedStockPriceParams params = PricePredictionBuilder.determineParams(pm.buildRelDayIndexSortedList(),
						minDayIndexPrediction.predictionDayIndex);
				
				SmoothedStockPrices smoothedStockPrices = new SmoothedStockPrices(params.minDayIndex, 
						pm.getRegressionModel().getCompany(), params.weights);
				
				result.put(pm, smoothedStockPrices);
			}
		}
		
		return result;
	}
}
