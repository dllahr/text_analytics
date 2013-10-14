package main;

import static org.junit.Assert.*;

import java.text.ParseException;


import org.junit.Test;

import controller.util.CommandLineParserUnrecognizedTokenException;


public class MainGeneratePrincipalComponentValuesTest {

	@Test
	public void testGetMostRecent() {
		System.out.println(MainGeneratePrincipalComponentValues.getMostRecentDayIndexOfArticleWithPrincipalComponentValue(1));
		assertTrue(true);
	}
	
	@Test
	public void testWithFakeScoringModelInDb() throws ParseException, CommandLineParserUnrecognizedTokenException {
		String[] args = {"5", "4", "-min_d", "1970-01-01", "-noSave", "-print"};
		
		MainGeneratePrincipalComponentValues.main(args);
	}
}
