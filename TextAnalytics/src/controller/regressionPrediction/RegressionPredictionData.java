package controller.regressionPrediction;

import java.util.Date;

import orm.RegressionModel;

public class RegressionPredictionData {
	private final RegressionModel regressionModel;
	
	private final Date date;
	
	private final int dayIndex;
	
	private final double value;

	public RegressionPredictionData(RegressionModel regressionModel, Date date,
			int dayIndex, double value) {
		this.regressionModel = regressionModel;
		this.date = date;
		this.dayIndex = dayIndex;
		this.value = value;
	}

	public RegressionModel getRegressionModel() {
		return regressionModel;
	}

	public Date getDate() {
		return date;
	}

	public int getDayIndex() {
		return dayIndex;
	}

	public double getValue() {
		return value;
	}
}
