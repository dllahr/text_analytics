package controller.prediction.regressionModel;

public class DayIndexRawPredictionPair {
	public final int initialDayIndex;
	public final int predictionDayIndex;
	public final double prediction;
	
	public DayIndexRawPredictionPair(int initialDayIndex, int predictionDayIndex, double prediction) {
		this.initialDayIndex = initialDayIndex;
		this.predictionDayIndex = predictionDayIndex;
		this.prediction = prediction;
	}
}
