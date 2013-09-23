package math.linearAlgebra;

import java.util.Set;

public interface Vector {

	public void setEntry(Integer index, Double value);

	public double getEntry(Integer index);

	public double vectorMultiply(Vector otherVector);

	public double vectorMultiply(double doubleVector[]);

	public Vector scalarMultiply(double scalar);
	
	public Set<Integer> getIndices();

	public int getMaxIndex();
	
	public Vector negate();
	
	public Vector createCopy();
	
	public double sum();
}