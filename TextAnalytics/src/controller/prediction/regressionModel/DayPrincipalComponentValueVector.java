package controller.prediction.regressionModel;

import math.linearAlgebra.Vector;

public class DayPrincipalComponentValueVector {
	public final int dayIndex;
	
	public final Vector prinCompValueVect;

	public DayPrincipalComponentValueVector(int dayIndex,
			Vector prinCompValueVect) {
		this.dayIndex = dayIndex;
		this.prinCompValueVect = prinCompValueVect;
	}
}
