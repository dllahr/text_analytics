package math.linearAlgebra;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VectorTest {
	private static final double eps = 1e-5;
	
	private static final double[] valueArray = {0.0, 2.0, 3.0, -1.0, -5.0};

	private Vector[] vectors;
	
	@Before
	public void setup() {
		vectors = buildTestVectors();
	}
	
	public static Vector[] buildTestVectors() {
		Vector[] vectors = new Vector[2];
		vectors[0] = new DenseFixedVector(valueArray);
		vectors[1] = new SparseVector();
		for (int i = 0; i < valueArray.length; i++) {
			vectors[1].setEntry(i, valueArray[i]);
		}
		
		return vectors;
	}
	
	@Test 
	public void testNegate() {
		for (Vector v : vectors) {
			Vector n = v.negate();
			
			for (int i = 0; i < valueArray.length; i++) {
				assertEquals(-valueArray[i], n.getEntry(i), eps);
			}
		}
	}

	@Test 
	public void testCreateCopy() {
		for (Vector v: vectors) {
			Vector c = v.createCopy();
			
			for (int i = 0; i < valueArray.length; i++) {
				assertEquals(valueArray[i], c.getEntry(i), eps);
			}
		}
	}
	
	@Test
	public void testSum() {
		double sum = 0.0;
		for (double val : valueArray) {
			sum += val;
		}
		
		for (Vector v : vectors) {
			assertEquals(sum, v.sum(), eps);
		}
	}
	
	@Test
	public void testScalarMultiply() {		
		final double scalar = 5.0;
		
		for (Vector v : vectors) {
			Vector v2 = v.scalarMultiply(scalar);
			
			assertEquals(v.getMaxIndex(), v2.getMaxIndex());
			
			for (int i = 0; i < valueArray.length; i++) {
				assertEquals(scalar*valueArray[i], v2.getEntry(i), eps);
			}
		}
	}
}
