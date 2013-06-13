package controller.prediction.regressionModel;

public class DayIndexPredictionPair {
	public final int initialDayIndex;
	public final int predictionDayIndex;
	public final double prediction;
	
	public DayIndexPredictionPair(int initialDayIndex, int predictionDayIndex, double prediction) {
		this.initialDayIndex = initialDayIndex;
		this.predictionDayIndex = predictionDayIndex;
		this.prediction = prediction;
	}
}
