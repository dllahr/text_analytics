package research.dji;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import orm.DjiCorr;

public class RegressionTest {

	@Test
	public void test() {
		List<DjiCorr> list = Regression.getDjiCorr();
		assertTrue(list.size() > 0);
		
		for (DjiCorr djiCorr : list) {
			System.out.println(djiCorr.getDayOffset() + " " + djiCorr.getEigenvalue().getId() + " " + djiCorr.getAverageCorrelation());
		}
	}

}
