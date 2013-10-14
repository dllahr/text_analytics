package math.linearAlgebra;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class OperationsTest {
	private static final double eps = 1e-5;

	@SuppressWarnings("unused")
	@Test
	public void testCalculateLowerTriangleDistanceMatrix() {
		double[] val0 = {0.0, 1.0, 2.0};
		double[] val1 = {3.0, 5.0, 7.0};
		double dist01 = Math.sqrt(3.0*3.0 + 4.0*4.0 + 5.0*5.0);
		
		double[] val2 = {11.0, 13.0, 17.0};
		double dist02 = Math.sqrt(11.0*11.0 + 12.0*12.0 + 15.0*15.0);
		double dist12 = Math.sqrt(8.0*8.0 + 8.0*8.0 + 10.0*10.0);
		
		Vector v0 = new DenseFixedVector(val0);
		Vector v1 = new DenseFixedVector(val1);
		Vector v2 = new DenseFixedVector(val2);
		
		List<Vector> vectorList = new LinkedList<Vector>();
		vectorList.add(v0);
		vectorList.add(v1);
		vectorList.add(v2);
		vectorList.add(v1);
		
		Matrix result = Operations.calculateUpperTriangleDistanceMatrix(vectorList);
		assertNotNull(result);
		assertEquals(vectorList.size(), result.getNumRows());
		assertEquals(vectorList.size(), result.getNumCols());
		
		if (false) {
			for (int rowInd = 0; rowInd < result.getNumRows(); rowInd++) {
				for (int colInd = 0; colInd < result.getNumCols(); colInd++) {
					System.out.print(result.getEntry(rowInd, colInd) + " ");
				}
				System.out.println();
			}
		}
		
		assertEquals(dist01, result.getEntry(0, 1), eps);
		assertEquals(dist02, result.getEntry(0, 2), eps);
		assertEquals(dist12, result.getEntry(1, 2), eps);
		
		assertEquals(dist01, result.getEntry(0, 3), eps);
		assertEquals(0.0, result.getEntry(1, 3), eps);
		assertEquals(dist12, result.getEntry(2, 3), eps);
	}
	
	
	@Test
	public void testAddVectors() {
		double[] val1 = {0.0, 1.0, 2.0, 3.0, 5.0};
		double[] val2 = {7.0, 11, 13.0, 17.0, 19.0, 23.0};
		
		Vector v1 = new DenseFixedVector(val1);
		Vector v2 = new DenseFixedVector(val2);
		
		Vector sum = Operations.addVectors(v1, v2);
		
		for (int i = 0; i < val1.length; i++) {
			assertEquals(val1[i] + val2[i], sum.getEntry(i), eps);
		}
		assertEquals(val2[val2.length-1], sum.getEntry(val2.length-1), eps);
		
		Vector v3 = new SparseVector();
		for (int i = 0; i < val1.length; i++) {
			v3.setEntry(i, val1[i]);
		}
		sum = Operations.addVectors(v3, v2);
		for (int i = 0; i < val1.length; i++) {
			assertEquals(val1[i] + val2[i], sum.getEntry(i), eps);
		}
		assertEquals(val2[val2.length-1], sum.getEntry(val2.length-1), eps);
		
		Vector v4 = new SparseVector();
		for (int i = 0; i < val2.length; i++) {
			v4.setEntry(i, val2[i]);
		}
		sum = Operations.addVectors(v1, v4);
		for (int i = 0; i < val1.length; i++) {
			assertEquals(val1[i] + val2[i], sum.getEntry(i), eps);
		}
		assertEquals(val2[val2.length-1], sum.getEntry(val2.length-1), eps);
		
		sum = Operations.addVectors(v3, v4);
		for (int i = 0; i < val1.length; i++) {
			assertEquals(val1[i] + val2[i], sum.getEntry(i), eps);
		}
		assertEquals(val2[val2.length-1], sum.getEntry(val2.length-1), eps);
	}
}
