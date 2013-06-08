package math.linearAlgebra;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class SparseVectorTest {
	private static final double eps = 1e-5;
	
	@Test
	public void testAddSetEntry() {
		final int index = 11;
		final double value = 1.3;
		SparseVector v = new SparseVector();
		v.setEntry(index, value);
		
		assertEquals(0.0, v.getEntry(0), eps);
		assertEquals(0.0, v.getEntry(12), eps);
		assertEquals(value, v.getEntry(index), eps);
	}
	
	
	@Test
	public void testGetIndices() {
		double[] v1Array = {0, 0, 1.1, 2.2, 3.3, 0, 4.4};
		int[] v1IndArray = {2,3,4,6};
		int[] notIndArray = {1,7};
		
		SparseVector v1 = new SparseVector();
		for (int index : v1IndArray) {
			v1.setEntry(index, v1Array[index]);
		}

		Set<Integer> indices = v1.getIndices();
		for (int index : v1IndArray) {
			assertTrue(indices.contains(index));
		}
		
		for (int notIndex : notIndArray) {
			assertFalse(indices.contains(notIndex));
		}
		
		indices.add(notIndArray[0]);
		assertFalse(v1.getIndices().contains(notIndArray[0]));
		
	}
	
	@Test
	public void testVectorMultiply() {
		double[] v1Array = {0, 0, 1.1, 2.2, 3.3, 0, 4.4};
		int[] v1IndArray = {2,3,4,6};
		double[] v2Array = {5.5, 6.6, 7.7, 0, 0, 8.8, 9.9};
		int[] v2IndArray = {0,1,2,5,6};
		
		double expected = 0.0;
		for (int i = 0; i < v1Array.length; i++) {
			expected += v1Array[i] * v2Array[i];
		}
		System.out.println(expected);
		
		SparseVector v1 = new SparseVector();
		for (int index : v1IndArray) {
			v1.setEntry(index, v1Array[index]);
		}
		SparseVector v2 = new SparseVector();
		for (int index : v2IndArray) {
			v2.setEntry(index, v2Array[index]);
		}
		
		assertEquals(expected, v1.vectorMultiply(v2), eps);
		assertEquals(expected, v1.vectorMultiply(v2Array), eps);
	}

	@Test
	public void testVectorMultiplyShortArray() {
		double[] array = {1.1, 2.2};
		
		SparseVector v = new SparseVector();
		v.setEntry(3, 3.3);
		
		v.vectorMultiply(array);
	}
}
