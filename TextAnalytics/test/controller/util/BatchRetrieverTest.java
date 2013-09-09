package controller.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class BatchRetrieverTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testRetrieveAll() {
		GenericRetriever<Integer> genRet = new GenericRetriever<Integer>() {
			@Override
			public List retrieve(Collection<Integer> coll) {
				return new ArrayList<>(coll);
			}
		};
		
		BatchRetriever<Integer> br = new BatchRetriever<>(genRet);
		
		List<Integer> inputList = new LinkedList<>();
		for (int i = 0; i < 1000; i++) {
			inputList.add(i);
		}
		
		List result = br.retrieveAll(inputList);
		assertNotNull(result);
		assertEquals(inputList.size(), result.size());
		assertTrue(result.get(0) instanceof Integer);
		
		inputList.add(1000);
		result = br.retrieveAll(inputList);
		assertNotNull(result);
		assertEquals(inputList.size(), result.size());
		
		for (int i = 1001; i < 1500; i++) {
			inputList.add(i);
		}
		result = br.retrieveAll(inputList);
		assertNotNull(result);
		assertEquals(inputList.size(), result.size());
		
		for (int i = 0; i < 1500; i++) {
			assertEquals(inputList.get(i), result.get(i));
		}
	}

}
