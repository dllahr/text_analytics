package math.linearAlgebra;

import java.util.Set;

public class DenseFixedVector implements Vector {

	private final double[] entryArray;
	
	public DenseFixedVector(int length) {
		entryArray = new double[length];
	}
	
	public DenseFixedVector(double[] entryArray) {
		this.entryArray = new double[entryArray.length];
		
		for (int i = 0; i < entryArray.length; i++) {
			this.entryArray[i] = entryArray[i];
		}
	}

	@Override
	public void setEntry(Integer index, Double value) {
		entryArray[index] = value;
	}

	@Override
	public double getEntry(Integer index) {
		if (index < entryArray.length) {
			return entryArray[index];
		} else {
			return 0.0;
		}
	}

	@Override
	public double vectorMultiply(Vector otherVector) {
		double result = 0.0;
		
		for (int i = 0; i < entryArray.length; i++) {
			result += entryArray[i] * otherVector.getEntry(i);
		}
		
		return result;
	}

	@Override
	public double vectorMultiply(double[] doubleVector) {
		double result = 0.0;
		
		final int maxIndex = doubleVector.length > entryArray.length ? entryArray.length : doubleVector.length;
		
		for (int i = 0; i < maxIndex; i++) {
			result += entryArray[i] * doubleVector[i];
		}
		
		return result;
	}

	@Override
	public Set<Integer> getIndices() {
		throw new UnsupportedOperationException("DenseFixedVector getIndices");
	}

	@Override
	public int getMaxIndex() {
		return entryArray.length - 1;
	}

}
