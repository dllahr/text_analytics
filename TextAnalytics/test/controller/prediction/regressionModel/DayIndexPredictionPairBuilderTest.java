package controller.prediction.regressionModel;

import static org.junit.Assert.*;

import math.linearAlgebra.Vector;

import org.junit.Test;

public class DayIndexPredictionPairBuilderTest {

	@Test
	public void testBuildRegressionModelCoefVector() {
		Vector vect = DayIndexPredictionPairBuilder.buildRegressionModelCoefVector(1);
		assertNotNull(vect);
		assertTrue(vect.getMaxIndex() > 0);
		
		System.out.println("max index:  " + vect.getMaxIndex());
		
		for (int i = 0; i <= vect.getMaxIndex(); i++) {
			System.out.println(vect.getEntry(i));
		}
	}

	@Test
	public void testGetRegressionModelIntercept() {
		double intercept = DayIndexPredictionPairBuilder.getRegressionModelIntercept(1);
		System.out.println(intercept);
	}
	
	@Test
	public void testGetRegressionModelDayOffset() {
		int dayOffset = DayIndexPredictionPairBuilder.getRegressionModelDayOffset(1);
		assertTrue(dayOffset >= 0);
		System.out.println(dayOffset);
	}

}
