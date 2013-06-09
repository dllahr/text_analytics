package controller.prediction.principalComponent;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import orm.Eigenvalue;

public class PrincipalComponentVectorBuilderTest {

	private static final double eps = 1e-5;
	
	@Test
	public void testProcessRawValues() {
		List<Object[]> rowList = new LinkedList<>();
		
		Object[] row = new Object[2];
		row[0] = new BigDecimal(1);
		row[1] = new BigDecimal(1.1);
		rowList.add(row);
		
		row = new Object[2];
		row[0] = new BigDecimal(1);
		row[1] = new BigDecimal(2.2);
		rowList.add(row);
		
		row = new Object[3];
		row[0] = new BigDecimal(3);
		row[1] = new BigDecimal(3.3);
		rowList.add(row);
		
		List<PrincipalComponentVector> list = PrincipalComponentVectorBuilder.processRawValues(rowList, 2);
		assertNotNull(list);
		assertEquals(2, list.size());
		
		PrincipalComponentVector pcv = list.get(0);
		assertNotNull(pcv);
		assertNotNull(pcv.eigenvalue);
		assertEquals(1, (long)pcv.eigenvalue.getId());
		assertEquals(1, pcv.vector.getMaxIndex());
		assertEquals(1.1, pcv.vector.getEntry(0), eps);
		assertEquals(2.2, pcv.vector.getEntry(1), eps);
		
		pcv = list.get(1);
		assertNotNull(pcv);
		assertNotNull(pcv.eigenvalue);
		assertEquals(3, (long)pcv.eigenvalue.getId());
		assertEquals(1, pcv.vector.getMaxIndex());
		assertEquals(3.3, pcv.vector.getEntry(0), eps);
		assertEquals(0.0, pcv.vector.getEntry(1), eps);
	}
	
	@Test
	public void testFindEigenvalue() {
		Eigenvalue e = PrincipalComponentVectorBuilder.findEigenvalue(1);
		assertNotNull(e);
	}
	
	@Test
	public void testRetrieveRawValues() {
		List<Object[]> list = PrincipalComponentVectorBuilder.retrieveRawValues(1);
		assertNotNull(list);
		assertTrue(list.size() > 0);
		
		Object[] row = list.get(0);
		assertEquals(2, row.length);
		
		assertTrue(row[0] instanceof BigDecimal);
		assertTrue(row[1] instanceof BigDecimal);
		
		
		for (int i = 0; i < 10; i++) {
			row = list.get(i);
			System.out.println(row[0] + " " + row[1]);
		}
		for (int i = list.size()-10; i < list.size(); i++) {
			row = list.get(i);
			System.out.println(row[0] + " " + row[1]);
		}
	}

}
