package math.linearAlgebra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SparseVector implements Vector {

	private int maxIndex = 0;
	
	private Map<Integer, Double> data;
	
	public SparseVector() {
		data = new HashMap<Integer, Double>();
	}
	
	@Override
	public void setEntry(Integer index, Double value) {
		data.put(index, value);
		if (index > maxIndex) {
			maxIndex = index;
		}
	}
	
	@Override
	public double getEntry(Integer index) {
		Double value = data.get(index);
		if (null == value) {
			value = 0.0;
		}
		return (double)value;
	}
	

	@Override
	public double vectorMultiply(Vector otherVector) {
		double result = 0.0;
		for (Integer index : data.keySet()) {
			result += data.get(index) * otherVector.getEntry(index);
		}
		return result;
	}
	
	@Override
	public double vectorMultiply(double otherVector[]) {
		final int otherVectorLength = otherVector.length;

		double result = 0.0;
		for (Integer index : data.keySet()) {
			if (index < otherVectorLength) {
				result += data.get(index) * otherVector[index];
			}
		}

		return result;
	}
	
	@Override
	public Vector scalarMultiply(double scalar) {
		SparseVector result = new SparseVector();
		
		for (Integer index : data.keySet()) {
			final double value = scalar * data.get(index);
			
			result.setEntry(index, value);
		}
		
		return result;
	}

	@Override
	public Set<Integer> getIndices() {
		return new HashSet<Integer>(data.keySet());
	}
	
	@Override
	public int getMaxIndex() {
		return maxIndex;
	}

	@Override
	public Vector negate() {
		SparseVector neg = new SparseVector();
		
		for (Integer index : data.keySet()) {
			neg.setEntry(index, -data.get(index));
		}
		
		return neg;
	}


	@Override
	public Vector createCopy() {
		SparseVector result = new SparseVector();
		
		result.data.putAll(data);
		
		return result;
	}
	
	@Override
	public double sum() {
		double sum = 0.0;
		
		for (Double value : data.values()) {
			sum += value;
		}
		
		return sum;
	}
}
