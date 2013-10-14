package math.linearAlgebra;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
	
	
	public static Matrix calculateUpperTriangleDistanceMatrix(List<Vector> vectorList) {
		Matrix result = new NonSparseMatrix();
		
		int progress = 0;
		
		int rowIndex = 0;
		ListIterator<Vector> iter1 = vectorList.listIterator();
		while (iter1.hasNext()) {
			Vector v1 = iter1.next();
			v1 = v1.negate();
			
			int colIndex = rowIndex + 1;
			if (colIndex < vectorList.size()) {
				
				Iterator<Vector> iter2 = vectorList.listIterator(colIndex);
				while (iter2.hasNext()) {

					Vector diff = addVectors(v1, iter2.next());
					
					double dist = Math.sqrt(diff.vectorMultiply(diff));
					
					result.setEntry(rowIndex, colIndex, dist);
					
					colIndex++;
					
					progress++;
					
					if (progress%10000 == 0) {
						System.out.println("distance matrix calc progress:  " + progress);
					}
				}
			}
			
			rowIndex++;
		}
		
		result.setEntry(vectorList.size()-1, vectorList.size()-1, 0.0);
		
		return result;
	}
}
