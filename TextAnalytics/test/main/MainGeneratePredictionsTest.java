package main;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	@Test
	public void testRetrieveArticleIdsForMinDateAndScoringModel() throws ParseException {
		final DateFormat dateFormat = new SimpleDateFormat(MainGeneratePredictions.dateFormatString);
		Date minDate = dateFormat.parse("2013-08-20");
		
		List<Integer> articleIdList = MainGeneratePredictions.retrieveArticleIdsForMinDateAndScoringModel(minDate, null, 1);
		assertNotNull(articleIdList);
		assertTrue(articleIdList.size() > 0);
		
		for (Integer id : articleIdList) {
			System.out.println(id);
		}
		
		Date maxDate = dateFormat.parse("2013-08-23");
		articleIdList = MainGeneratePredictions.retrieveArticleIdsForMinDateAndScoringModel(minDate, maxDate, 1);
		assertNotNull(articleIdList);
		assertEquals(6, articleIdList.size());
	}
}
