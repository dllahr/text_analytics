package controller.articleIntegration;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import math.linearAlgebra.DenseFixedVector;
import math.linearAlgebra.Vector;

import org.junit.Test;

import controller.stemCountArticles.ArticleStemCountVector;
import controller.util.Triple;

import orm.Article;

public class CompareArticleStemCountsTest {

	private static final double eps = 1e-5;
	
	@Test
	public void testCompare() {
		List<Integer> articleIdList = new LinkedList<>();
		articleIdList.add(90570);
		articleIdList.add(90571);
		articleIdList.add(90572);
		
		List<Triple<Article,Article,Double>> result = (new CompareArticleStemCounts()).compare(articleIdList);
		assertNotNull(result);
		assertEquals(3, result.size());
		
		for (Triple<Article,Article,Double> triple : result) {
			System.out.println(triple.t.getId() + " " + triple.u.getId() + " " + triple.v);
		}
		
	}

	@Test
	public void testCompareVectors() {
		Article a1 = new Article();
		a1.setId(1);
		Vector v1 = new DenseFixedVector(new double[]{0.0, 2.0});
		ArticleStemCountVector ascv1 = new ArticleStemCountVector(a1, v1);
		
		Article a2 = new Article();
		a2.setId(2);
		Vector v2 = new DenseFixedVector(new double[]{3.0, 5.0});
		ArticleStemCountVector ascv2 = new ArticleStemCountVector(a2, v2);
		double dist12 = Math.sqrt(3.0*3.0 + 3.0*3.0);
		
		Article a3 = new Article();
		a3.setId(3);
		Vector v3 = new DenseFixedVector(new double[]{7.0, 11.0});
		ArticleStemCountVector ascv3 = new ArticleStemCountVector(a3, v3);
		double dist13 = Math.sqrt(7.0*7.0 + 9.0*9.0);
		double dist23 = Math.sqrt(4.0*4.0 + 6.0*6.0);
		
		List<ArticleStemCountVector> list = new LinkedList<>();
		list.add(ascv1);
		list.add(ascv2);
		list.add(ascv3);
		
		List<Triple<Article,Article,Double>> result = CompareArticleStemCounts.compareVectors(list);
		assertNotNull(result);
		assertEquals(3, result.size());
		
		Triple<Article,Article,Double> triple = result.get(0);
		assertEquals(a1, triple.t);
		assertEquals(a2, triple.u);
		assertEquals(dist12, triple.v, eps);
		
		triple = result.get(1);
		assertEquals(a1, triple.t);
		assertEquals(a3, triple.u);
		assertEquals(dist13, triple.v, eps);
		
		triple = result.get(2);
		assertEquals(a2, triple.t);
		assertEquals(a3, triple.u);
		assertEquals(dist23, triple.v, eps);
	}

}
