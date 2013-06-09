package controller.prediction.principalComponent;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import orm.Eigenvalue;

public class ArticlePrincipalComponentValuesCalculatorTest {

	@Test
	public void test() throws ParseException {
		ArticlePrincipalComponentValueCalculator calc = new ArticlePrincipalComponentValueCalculator();
		
		List<ArticlePrincipalComponentValues> list = 
				calc.calculate(1, (new SimpleDateFormat("yyyy-MM-dd").parse("2013-05-10")));
		
		assertNotNull(list);
		assertTrue(list.size() > 0);
		
		ArticlePrincipalComponentValues artPcv = list.get(0);
		assertEquals(artPcv.prinCompValuesMap.size(), 100);
		
		for (int i = 0; i < 10; i++) {
			for (Eigenvalue e : artPcv.prinCompValuesMap.keySet()) {
				if (e.getId() == i) {
					System.out.println(i + " " + artPcv.prinCompValuesMap.get(e));
					break;
				}
			}
		}
	}

}
