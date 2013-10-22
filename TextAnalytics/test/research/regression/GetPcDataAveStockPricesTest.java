package research.regression;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import orm.Eigenvalue;

public class GetPcDataAveStockPricesTest {

	@Test
	public void testPrintGlmExpression() {
		List<Eigenvalue> eigList = Eigenvalue.getEigenvalueByScoringModel(3);
		assertNotNull(eigList);
		
		Collections.sort(eigList, new Comparator<Eigenvalue>() {
			@Override
			public int compare(Eigenvalue o1, Eigenvalue o2) {
				return o1.getId() - o2.getId();
			}
		});
		
		GetPcDataAveStockPrices.printGlmExpression(eigList);
	}

}
