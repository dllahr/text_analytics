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
		StringBuilder builder = new StringBuilder();
		builder.append(initialDayIndex).append(Constants.toStringDelimeter);
		builder.append(predictionDayIndex).append(Constants.toStringDelimeter);
		builder.append(String.format("%.5G", prediction));
		
		return builder.toString();
	}
}
