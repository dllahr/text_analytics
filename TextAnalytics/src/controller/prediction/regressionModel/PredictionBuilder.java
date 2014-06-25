package controller.prediction.regressionModel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import orm.PredictionModel;

public abstract class PredictionBuilder {
	public abstract List<Prediction> build(Collection<PredictionModel> pmColl, List<DayIndexRawPredictionPair> rawPredColl);
	
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
}
