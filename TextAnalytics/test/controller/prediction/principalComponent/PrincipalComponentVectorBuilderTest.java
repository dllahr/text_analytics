package controller.prediction.principalComponent;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import orm.Eigenvalue;

public class PrincipalComponentVectorBuilderTest {

	private static final double eps = 1e-5;
	

	
	@Test
	public void testProcessRawValues() {
		int[] eigIds = {5, 6, 11, 1};
		int[] stemIds = {4, 9, 2, 3};
		int maxStemId = 9;
		int[][] stemIdIndexes = {{0,1,2,3}, {3,2,1,0}, {2,1,3,0}, {3,0,2,1}};
		
		List<Object[]> rowList = new LinkedList<>();
		Map<Integer, Eigenvalue> idEigMap = new HashMap<>();
		
		double value = 1.1;
		for (int i = 0; i < eigIds.length; i++) {
			int[] stemIndexes = stemIdIndexes[i];
			
			for (int stemInd : stemIndexes) {
				Object[] row = new Object[3];
				rowList.add(row);
				
				row[0] = new BigDecimal(eigIds[i]);
				row[1] = new BigDecimal(stemIds[stemInd]);
				row[2] = new BigDecimal(value);
				
				value += 1.1;
				
			}
			
			Eigenvalue eig = new Eigenvalue();
			eig.setId(eigIds[i]);
		}

		Set<Integer> stemIdSet = new HashSet<>();
		for (int stemId : stemIds) {
			stemIdSet.add(stemId);
		}
		
		Map<Integer, PrincipalComponentVector> result = PrincipalComponentVectorBuilder.processRawValues(rowList, maxStemId, idEigMap);
		assertNotNull(result);
		assertEquals(eigIds.length, result.size());
		
		for (int eigId : eigIds) {
			PrincipalComponentVector pcv = result.get(eigId);
			assertNotNull(pcv);
			
			System.out.println("eigId " + eigId);
			
			for (int i = 0; i < maxStemId; i++) {
				double entry = pcv.vector.getEntry(i);
			
				System.out.println(i + " " + entry);
				
				if (stemIdSet.contains(i+1)) {
					assertTrue(entry != 0.0);
				} else {
					assertEquals(0.0, entry, eps);
				}
			}
		}
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
