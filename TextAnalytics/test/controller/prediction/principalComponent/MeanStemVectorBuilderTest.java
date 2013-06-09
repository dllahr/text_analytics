package controller.prediction.principalComponent;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import orm.MeanStemCount;
import orm.Stem;

public class MeanStemVectorBuilderTest {

	@Test
	public void testRetrieveMeanVector() {
		System.out.println("testRetrieveMeanVector");

		List<MeanStemCount> list = (new MeanStemVectorBuilder()).retrieveMeanStemCounts(1);
		for (int i = 0; i < 10; i++) {
			if (i < list.size()) {
				MeanStemCount msc = list.get(i);
				System.out.println(msc.getStem().getId() + " " + msc.getValue());
			}
		}
	}
	
	@Test
	public void testBuildMeanVector() {
		System.out.println("testBuildMeanVector");

		MeanStemVectorBuilder builder = new MeanStemVectorBuilder();
		
		List<MeanStemCount> list = builder.retrieveMeanStemCounts(1);
		
		double[] v = builder.buildMeanVector(list);
		
		for (int i = 0; i < 10; i++) {
			if (i < v.length) {
				System.out.println(i + " " + v[i]);
			}
		}
	}

	@Test
	public void testGetMinStemId() {
		Stem oneStem = new Stem();
		oneStem.setId(1);
		Stem twoStem = new Stem();
		twoStem.setId(2);
		
		MeanStemCount oneMsc = new MeanStemCount(null, oneStem, Double.NaN);
		MeanStemCount twoMsc = new MeanStemCount(null, twoStem, Double.NaN);
		
		
		List<MeanStemCount> list = new LinkedList<>();
		list.add(oneMsc);
		list.add(twoMsc);
		
		MeanStemVectorBuilder builder = new MeanStemVectorBuilder();
		
		int minId = builder.getMinStemId(list);
		assertEquals(1, minId);
		
		Collections.reverse(list);
		minId = builder.getMinStemId(list);
		assertEquals(1, minId);
	}
}
