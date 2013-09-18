package math.linearAlgebra;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VectorTest {
	private static final double eps = 1e-5;
	
	private static final double[] val = {0.0, 2.0, 3.0, -1.0, -5.0};

	private Vector[] vectors;
	
	@Before
	public void setup() {
		vectors = new Vector[2];
		vectors[0] = new DenseFixedVector(val);
		vectors[1] = new SparseVector();
		for (int i = 0; i < val.length; i++) {
			vectors[1].setEntry(i, val[i]);
		}
	}
	
	@Test 
	public void testNegate() {
		for (Vector v : vectors) {
			Vector n = v.negate();
			
			for (int i = 0; i < val.length; i++) {
				assertEquals(-val[i], n.getEntry(i), eps);
			}
		}
	}

	@Test 
	public void testCreateCopy() {
		for (Vector v: vectors) {
			Vector c = v.createCopy();
			
			for (int i = 0; i < val.length; i++) {
				assertEquals(val[i], c.getEntry(i), eps);
			}
		}
	}
}
