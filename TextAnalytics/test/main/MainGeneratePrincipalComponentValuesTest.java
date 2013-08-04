package main;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import orm.Constants;

public class MainGeneratePrincipalComponentValuesTest {

	@Test
	public void testGetMostRecent() {
		System.out.println(MainGeneratePrincipalComponentValues.getMostRecentDayIndexOfArticleWithPrincipalComponentValue(1));
	}

	@Test
	public void testIncrementingDays() {
		final int dayIndex = 15913;
		
		long[] millis = {Constants.millisPerDay*dayIndex - 1, Constants.millisPerDay*dayIndex,
				Constants.millisPerDay*(dayIndex + 1) - 1, Constants.millisPerDay*(dayIndex+1)};
		
		for (long milli : millis) {
			System.out.println(milli + " " + new Date(milli));
		}
	}
}
