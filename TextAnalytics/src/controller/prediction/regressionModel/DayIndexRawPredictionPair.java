package controller.prediction.regressionModel;

import orm.Constants;

public class DayIndexRawPredictionPair {
	public final int initialDayIndex;
	public final int predictionDayIndex;
	public final double prediction;
	
	public DayIndexRawPredictionPair(int initialDayIndex, int predictionDayIndex, double prediction) {
		this.initialDayIndex = initialDayIndex;
		this.predictionDayIndex = predictionDayIndex;
		this.prediction = prediction;
	}

	@Override
	public String toString() {
		return initialDayIndex + Constants.toStringDelimeter + predictionDayIndex 
				+ Constants.toStringDelimeter + prediction;
	}
}
