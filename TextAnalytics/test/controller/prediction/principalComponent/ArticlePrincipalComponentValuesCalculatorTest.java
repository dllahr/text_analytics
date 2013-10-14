package controller.prediction.principalComponent;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import math.linearAlgebra.Operations;
import math.linearAlgebra.Vector;
import math.linearAlgebra.VectorTest;

import org.junit.Test;

import controller.stemCountArticles.ArticleStemCountVector;
import controller.stemCountArticles.ArticleStemCountVectorBuilder;

import orm.Article;
import orm.Eigenvalue;

public class ArticlePrincipalComponentValuesCalculatorTest {
	private static final double eps = 1e-5;

	@Test
	public void test() throws ParseException {
		final int articleSourceId = 1;
		final int scoringModelId = 1;
		final Date date = (new SimpleDateFormat("yyyy-MM-dd").parse("2013-05-10"));
		
		List<Integer> articleIdList = 
				Article.getArticleIdsForMinDateAndArticleSource(date, date, articleSourceId, false, false);
		
		ArticlePrincipalComponentValueCalculator calc = new ArticlePrincipalComponentValueCalculator();
		
		List<ArticlePrincipalComponentValues> list = calc.calculate(scoringModelId, articleIdList);
		
		assertNotNull(list);
		assertTrue(list.size() > 0);
		
		ArticlePrincipalComponentValues artPcv = list.get(0);
		assertEquals(artPcv.prinCompValuesMap.size(), 100);
		
		for (int i = 0; i < 10; i++) {
			for (Eigenvalue e : artPcv.prinCompValuesMap.keySet()) {
				if (e.getId().equals(i)) {
					System.out.println(i + " " + artPcv.prinCompValuesMap.get(e));
					break;
				}
			}
		}
	}
	
	@Test
	public void testSpecific() {
		final int scoringModelId = 1;
		List<Integer> articleIdList = new LinkedList<>();
		articleIdList.add(351);
		
		MeanStemCountVector meanVect = (new MeanStemCountVectorBuilder()).build(scoringModelId);
		
		List<ArticleStemCountVector> artStemCountVectList = (new ArticleStemCountVectorBuilder()).retrieve(articleIdList, 
				meanVect.minStemId, false);
		
		ArticleStemCountVector asc = artStemCountVectList.get(0);
		List<Integer> stemIndexList = new ArrayList<>(asc.stemCountVector.getIndices());
		Collections.sort(stemIndexList);
		for (Integer stemIndex : stemIndexList) {
			System.out.println(stemIndex + " " + asc.stemCountVector.getEntry(stemIndex));
		}
		
		Vector diff = Operations.addVectors(asc.stemCountVector.negate(), meanVect.meanVector);
		for (int i = 0; i < 10; i++) {
			System.out.println(diff.getEntry(i));
		}
		
		List<PrincipalComponentVector> pcVectorList = 
				(new PrincipalComponentVectorBuilder()).build(scoringModelId, 
						meanVect.meanVector.getMaxIndex() + 1);
		
		PrincipalComponentVector pcv = pcVectorList.get(21);
		System.out.println(pcv.eigenvalue.getId());
		
		double result = diff.vectorMultiply(pcv.vector);
		System.out.println(result);
	}
	
	@Test
	public void testCalculateNormalizedVector() {
		for (Vector v : VectorTest.buildTestVectors()) {
			Vector nv = ArticlePrincipalComponentValueCalculator.normalizeArticleStemCountVectorCounts(v);
		
			assertEquals(1.0, nv.sum(), eps);
		}
	}
}
