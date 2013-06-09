package controller.prediction.principalComponent;

import math.linearAlgebra.DenseFixedVector;
import math.linearAlgebra.Vector;

class MeanStemCountVector {
	public final Vector meanVector;
	
	public final int minStemId;

	public MeanStemCountVector(double[] meanVector, int minStemId) {
		this.meanVector = new DenseFixedVector(meanVector);
		this.minStemId = minStemId;
	}
}
