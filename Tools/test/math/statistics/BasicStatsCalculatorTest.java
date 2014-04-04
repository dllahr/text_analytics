package math.statistics;

import static org.junit.Assert.*;

import org.junit.Test;

public class BasicStatsCalculatorTest {

	private static final double eps = 1e-5;
	
	@Test
	public void testCalc() {
		double[] vals = {1.0, 2.0, 3.0, 5.0};
		BasicStats bs = BasicStatsCalculator.calc(vals);
		
		assertEquals(2.75, bs.ave, eps);
		assertEquals(1.479019946, bs.sdev, eps);
	}

}
