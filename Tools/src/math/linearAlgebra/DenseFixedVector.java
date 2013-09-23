package math.linearAlgebra;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DenseFixedVector implements Vector {
	
	private Set<Integer> indexSet = null;

	private final double[] entryArray;
	
	public DenseFixedVector(int length) {
		entryArray = new double[length];
	}
	
	public DenseFixedVector(double[] entryArray) {
		this(entryArray.length);
		
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
	public Vector scalarMultiply(double scalar) {
		DenseFixedVector result = new DenseFixedVector(entryArray.length);
		
		for (int i = 0; i < entryArray.length; i++) {
			final double value = scalar * entryArray[i];
			result.setEntry(i, value);
		}
		
		return result;
	}

	@Override
	public Set<Integer> getIndices() {
		if (null == indexSet) {
			indexSet = new HashSet<Integer>();
			for (int i = 0; i < entryArray.length; i++) {
				indexSet.add(i);
			}
			indexSet = Collections.unmodifiableSet(indexSet);
		}
		
		return indexSet;
	}

	@Override
	public int getMaxIndex() {
		return entryArray.length - 1;
	}

	@Override
	public Vector negate() {
		DenseFixedVector neg = new DenseFixedVector(entryArray.length);
		for (int i = 0; i < neg.entryArray.length; i++) {
			neg.entryArray[i] = - entryArray[i];
		}
		return neg;
	}

	public Vector add(Vector other) {
		final int length = entryArray.length > other.getMaxIndex()+1 ? entryArray.length : other.getMaxIndex()+1;
		DenseFixedVector sum = new DenseFixedVector(length);
		
		for (int i = 0; i < length; i++) {
			sum.entryArray[i] = getEntry(i) + other.getEntry(i);
		}
		
		return sum;
	}

	@Override
	public Vector createCopy() {
		return new DenseFixedVector(entryArray);
	}

	@Override
	public double sum() {
		double sum = 0.0;
		
		for (double value : entryArray) {
			sum += value;
		}
		
		return sum;
	}
}
