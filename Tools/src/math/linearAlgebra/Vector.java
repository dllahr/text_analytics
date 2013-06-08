package math.linearAlgebra;

import java.util.Set;

public interface Vector {

	public abstract void setEntry(Integer index, Double value);

	public abstract double getEntry(Integer index);

	public abstract double vectorMultiply(Vector otherVector);

	public abstract double vectorMultiply(double doubleVector[]);

	public abstract Set<Integer> getIndices();

	public abstract int getMaxIndex();

}