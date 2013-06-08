package math.linearAlgebra;

import static org.junit.Assert.*;

import org.junit.Test;

public class DenseFixedVectorTest {
	private static final double eps = 1e-5;

	@Test
	public void testAddSetEntry() {
		final int index = 11;
		final double value = 1.3;
		DenseFixedVector v = new DenseFixedVector(12);
		v.setEntry(index, value);
		
		assertEquals(0.0, v.getEntry(0), eps);
		assertEquals(0.0, v.getEntry(12), eps);
		assertEquals(value, v.getEntry(index), eps);
	}

	@Test
	public void testVectorMultiply() {
		double[] v1Array = {0, 0, 1.1, 2.2, 3.3, 0, 4.4};
		double[] v2Array = {5.5, 6.6, 7.7, 0, 0, 8.8, 9.9};
		
		double expected = 0.0;
		for (int i = 0; i < v1Array.length; i++) {
			expected += v1Array[i] * v2Array[i];
		}
		System.out.println(expected);
		
		DenseFixedVector v1 = new DenseFixedVector(v1Array);
		DenseFixedVector v2 = new DenseFixedVector(v2Array);
		
		assertEquals(expected, v1.vectorMultiply(v2), eps);
		assertEquals(expected, v1.vectorMultiply(v2Array), eps);
	}
}
