package controller.prediction.regressionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controller.stockPrices.FindStockPrices;
import orm.PredictionModel;
import orm.StockData;

public class ExtremaPredictionBuilder extends PredictionBuilder {

	@Override
	public List<Prediction> build(Collection<PredictionModel> pmColl, List<DayIndexRawPredictionPair> rawPredColl) {

		List<Prediction> result = new LinkedList<>();
		
		for (PredictionModel pm : pmColl) {
			List<DayIndexRawPredictionPair> filteredRawList = PredictionBuilder.filterRawPredictions(rawPredColl, pm);
			
			if (filteredRawList.size() > 0) {
				final int minDayIndex = filteredRawList.get(0).initialDayIndex;
				
				FindStockPrices findStockPrices = new FindStockPrices(minDayIndex, pm.getRegressionModel().getCompany());
				
				List<Integer> dayIndexList = new ArrayList<>(filteredRawList.size());
				for (DayIndexRawPredictionPair rawPair : filteredRawList) {
					dayIndexList.add(rawPair.initialDayIndex);
				}
				Map<Integer, StockData> map = findStockPrices.findNext(dayIndexList);
				
				for (DayIndexRawPredictionPair rawPair : filteredRawList) {
					StockData sd = map.get(rawPair.initialDayIndex);
					
					final double p25 = sd.getAdjustedClose() * Math.exp(pm.getPercentile25Value());
					final double p50 = sd.getAdjustedClose() * Math.exp(pm.getPercentile50Value());
					final double p75 = sd.getAdjustedClose() * Math.exp(pm.getPercentile75Value());
					
					result.add(new Prediction(rawPair.initialDayIndex, sd.getAdjustedClose(), 
							pm.getRegressionModel().getDayOffset() + rawPair.initialDayIndex, p25, p50, p75, pm));
				}
			} else {
				System.out.println("PredictionBuilder build none of the raw predictions passed the filter for prediction model:  " + pm.getId());
			}
		}
		
		return result;
	}
	
	
}
