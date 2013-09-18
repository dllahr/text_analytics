package math.linearAlgebra;

import static org.junit.Assert.*;

import org.junit.Test;

public class OperationsTest {
	private static final double eps = 1e-5;

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
