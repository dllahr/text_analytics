package math.linearAlgebra;

import java.util.HashSet;
import java.util.Set;

public class Operations {

	public static Vector addVectors(Vector v1, Vector v2) {
		if (v1 instanceof DenseFixedVector || v2 instanceof DenseFixedVector) {
			DenseFixedVector dense;
			Vector other;
			if (v1 instanceof DenseFixedVector) {
				dense = (DenseFixedVector)v1;
				other = v2;
			} else {
				dense = (DenseFixedVector)v2;
				other = v1;
			}
			
			return dense.add(other);
		} else {
			Vector result = new SparseVector();

			Set<Integer> indexSet = new HashSet<Integer>();
			indexSet.addAll(v1.getIndices());
			indexSet.addAll(v2.getIndices());

			for (Integer index : indexSet) {
				result.setEntry(index, v1.getEntry(index) + v2.getEntry(index));
			}

			return result;
		}
	}
}
