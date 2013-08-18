package main;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;



public class MainGeneratePredictionsTest {

	@Test
	public void testParsePredictionModelIdListParameter() {
		String param = "1,2,3,5,7,11";
		int[] expect = {1,2,3,5,7,11};
		
		List<Integer> result = MainGeneratePredictions.parsePredictionModelIdListParameter(param);
		assertNotNull(result);
		assertTrue(result.size() == expect.length);
		for (int i = 0; i < expect.length; i++) {
			assertEquals((long)expect[i], (long)result.get(i));
		}
		
		param = "1,2,3,5, 7, ";
		expect = new int[]{1,2,3,5,7};
		result = MainGeneratePredictions.parsePredictionModelIdListParameter(param);
		assertNotNull(result);
		assertTrue(result.size() == expect.length);
		for (int i = 0; i < expect.length; i++) {
			assertEquals((long)expect[i], (long)result.get(i));
		}
		
		param = "3";
		expect = new int[]{3};
		result = MainGeneratePredictions.parsePredictionModelIdListParameter(param);
		assertNotNull(result);
		assertTrue(result.size() == expect.length);
		for (int i = 0; i < expect.length; i++) {
			assertEquals((long)expect[i], (long)result.get(i));
		}
	}
	
	

	
}
