package controller.util;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class MapBuilderTest {

	@Test
	public void testBuild() {
		
		ValueOperator<Pair, Integer, List<Double>> vo = new ValueOperator<MapBuilderTest.Pair, Integer, List<Double>>() {
			@Override
			public Integer getKey(Pair t) {
				return t.i;
			}
			@Override
			public List<Double> buildNew() {
				return new LinkedList<>();
			}
			@Override
			public void addValue(Pair t, List<Double> aggregation) {
				aggregation.add(t.v);
			}
		};
		
		List<Pair> pairList = new LinkedList<>();
		pairList.add(new Pair(1, 1.1));
		pairList.add(new Pair(1, 2.2));
		pairList.add(new Pair(1,3.3));
		pairList.add(new Pair(2,4.4));
		pairList.add(new Pair(3,5.5));
		pairList.add(new Pair(4,6.6));
		pairList.add(new Pair(4,7.7));
		
		Map<Integer, List<Double>> map = (new MapBuilder()).build(vo, pairList);
		
		List<Double> valList = map.get(1);
		assertNotNull(valList);
		assertEquals(3, valList.size());
		
		valList = map.get(2);
		assertNotNull(valList);
		assertEquals(1, valList.size());
		
		valList = map.get(3);
		assertNotNull(valList);
		assertEquals(1, valList.size());
		
		valList = map.get(4);
		assertNotNull(valList);
		assertEquals(2, valList.size());
	}

	private class Pair {
		public final int i;
		public final double v;
		
		public Pair(int i, double v) {
			this.i = i;
			this.v = v;
		}
	}
}
