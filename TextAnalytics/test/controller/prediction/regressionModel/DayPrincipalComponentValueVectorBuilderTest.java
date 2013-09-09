package controller.prediction.regressionModel;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.junit.Test;

import controller.prediction.regressionModel.DayPrincipalComponentValueVectorBuilder.DayEigenvalue;
import controller.util.Utilities;

import orm.Article;
import orm.ArticlePcValue;
import orm.Eigenvalue;
import orm.SessionManager;

public class DayPrincipalComponentValueVectorBuilderTest {

	private static final double eps = 1e-5;
	
	@Test
	public void testRetrieveArticlePcValues() {
		List<Integer> eigIdList = new ArrayList<>();
		for (int i = 101; i <= 200; i++) {
			eigIdList.add(i);
		}
		
		final int articleId = 69198;
		List<Integer> articleIdList = new LinkedList<>();
		articleIdList.add(articleId);

		List<ArticlePcValue> list = DayPrincipalComponentValueVectorBuilder.retrieveArticlePcValues(articleIdList, eigIdList);
		assertNotNull(list);

		assertEquals(100, list.size());
		
		assertEquals((long)articleId, (long)list.get(0).getArticle().getId());
	}
	
	@Test
	public void testRetrieveArticlePcValuesMoreThan1000Articles() {
		List<Integer> eigIdList = new ArrayList<>();
		for (int i = 101; i <= 200; i++) {
			eigIdList.add(i);
		}
		
		Query query = SessionManager.createSqlQuery("select distinct article_id from article_pc_value where eigenvalue_id in (:eigIdList)");
		query.setParameterList("eigIdList", eigIdList);
		@SuppressWarnings("rawtypes")
		List rawArticleIdList = query.list();
		List<Integer> articleIdList = new ArrayList<>(rawArticleIdList.size());
		for (Object row : rawArticleIdList) {
			articleIdList.add(((BigDecimal)row).intValueExact());
		}

		assertNotNull(articleIdList);
		assertTrue(articleIdList.size() > 1000);
		
		List<ArticlePcValue> list = DayPrincipalComponentValueVectorBuilder.retrieveArticlePcValues(articleIdList, eigIdList);
		assertNotNull(list);
		
		List<ArticlePcValue> exampleList = new LinkedList<>();
		Set<Integer> exampleArticleIdSet = new HashSet<>();
		exampleArticleIdSet.add(articleIdList.get(0));
		exampleArticleIdSet.add(articleIdList.get(articleIdList.size()-1));
		
		Set<Integer> foundArticleIdSet = new HashSet<>();
		Set<Integer> foundEigIdSet = new HashSet<>();
		for (ArticlePcValue apv : list) {
			final int articleId = apv.getArticle().getId();
			foundArticleIdSet.add(articleId);
			foundEigIdSet.add(apv.getEigenvalue().getId());
			
			if (exampleArticleIdSet.contains(articleId)) {
				exampleList.add(apv);
			}
		}
		
		assertEquals(articleIdList.size(), foundArticleIdSet.size());
		for (Integer articleId : articleIdList) {
			assertTrue(foundArticleIdSet.contains(articleId));
		}
		
		assertEquals(eigIdList.size(), foundEigIdSet.size());
		for (Integer eigId : eigIdList) {
			assertTrue(foundEigIdSet.contains(eigId));
		}
		
		Collections.sort(exampleList, new Comparator<ArticlePcValue>() {
			@Override
			public int compare(ArticlePcValue o1, ArticlePcValue o2) {
				if (o1.getArticle().getId() == o2.getArticle().getId()) {
					return o1.getEigenvalue().getId() - o2.getEigenvalue().getId();
				} else {
					return o1.getArticle().getId() - o2.getArticle().getId();
				}
			}
		});
		for (ArticlePcValue apv : exampleList) {
			System.out.println(apv);
		}
	}

	
	@Test
	public void testDayEigenvalueClass() {
		Article day1 = new Article();
		day1.setDayIndex(1);
		
		Eigenvalue e1 = new Eigenvalue();
		
		DayEigenvalue de = new DayEigenvalue(day1.getDayIndex(), e1);
		assertEquals(de.hashCode(), (new DayEigenvalue(day1.getDayIndex(), e1)).hashCode());
		assertEquals(de, (new DayEigenvalue(day1.getDayIndex(), e1)));
		
		Set<DayEigenvalue> set = new HashSet<>();
		set.add(de);

		assertTrue(set.contains(new DayEigenvalue(day1.getDayIndex(), e1)));
	}
	
	@Test
	public void testDayEigenvalueClassExtended() {
		List<Eigenvalue> eigList = Utilities.convertGenericList(SessionManager.createQuery("from Eigenvalue where scoringModel.id = 1").list());
		
		Set<DayEigenvalue> set = new HashSet<>();

		for (int dayInd = 0; dayInd < 100; dayInd++) {
			for (Eigenvalue eig : eigList) {
				DayEigenvalue de = new DayEigenvalue(dayInd, eig);
				assertEquals(de.hashCode(), (new DayEigenvalue(dayInd, eig)).hashCode());
				assertEquals(de, new DayEigenvalue(dayInd, eig));
				
				set.add(de);
			}
		}
		
		for (int dayInd = 0; dayInd < 100; dayInd++) {
			for (Eigenvalue eig : eigList) {
				DayEigenvalue de = new DayEigenvalue(dayInd, eig);
				assertTrue(set.contains(de));
			}
		}
	}
	
	@Test
	public void testOrganizePcValByDayIndex() {
		Article day1 = new Article();
		day1.setDayIndex(1);
		Article day2 = new Article();
		day2.setDayIndex(2);
		
		Eigenvalue e1 = new Eigenvalue();
		Eigenvalue e2 = new Eigenvalue();
		
		List<ArticlePcValue> list = new LinkedList<>();
		list.add(new ArticlePcValue(day1, e1, 1.1));
		list.add(new ArticlePcValue(day1, e1, 2.2));
		list.add(new ArticlePcValue(day1, e2, 3.3));
		list.add(new ArticlePcValue(day1, e2, 4.4));
		list.add(new ArticlePcValue(day1, e2, 5.5));
		
		list.add(new ArticlePcValue(day2, e1, 6.6));
		list.add(new ArticlePcValue(day2, e2, 7.7));
		list.add(new ArticlePcValue(day2, e2, 8.8));
		
		Map<DayEigenvalue, List<Double>> map = (new DayPrincipalComponentValueVectorBuilder()).organizePcValByDayIndex(list);
		assertNotNull(map);
		assertEquals(4, map.keySet().size());

		DayEigenvalue de11 = new DayEigenvalue(day1.getDayIndex(), e1);
		assertTrue(map.keySet().contains(de11));
		List<Double> vals = map.get(de11);
		assertEquals(2, vals.size());
		assertEquals(1.1, vals.get(0), eps);
		assertEquals(2.2, vals.get(1), eps);
		
		DayEigenvalue de12 = new DayEigenvalue(day1.getDayIndex(), e2);
		assertTrue(map.keySet().contains(de12));
		assertEquals(3, map.get(de12).size());
		
		DayEigenvalue de21 = new DayEigenvalue(day2.getDayIndex(), e1);
		assertTrue(map.keySet().contains(de21));
		assertEquals(1, map.get(de21).size());
		
		DayEigenvalue de22 = new DayEigenvalue(day2.getDayIndex(), e2);
		assertTrue(map.keySet().contains(de22));
		assertEquals(2, map.get(de22).size());
	}
	
	@Test
	public void testAggregateByDay() {
		Article day1 = new Article();
		day1.setDayIndex(1);
		Article day2 = new Article();
		day2.setDayIndex(2);
		
		Eigenvalue e1 = new Eigenvalue();
		e1.setSortIndex(0);
		Eigenvalue e2 = new Eigenvalue();
		e2.setSortIndex(1);
		
		List<ArticlePcValue> list = new LinkedList<>();
		list.add(new ArticlePcValue(day1, e1, 1.1));
		list.add(new ArticlePcValue(day1, e1, 2.2));
		list.add(new ArticlePcValue(day1, e2, 3.3));
		list.add(new ArticlePcValue(day1, e2, 4.4));
		list.add(new ArticlePcValue(day1, e2, 5.5));
		
		list.add(new ArticlePcValue(day2, e1, 6.6));
		list.add(new ArticlePcValue(day2, e2, 7.7));
		list.add(new ArticlePcValue(day2, e2, 8.8));
		
		DayPrincipalComponentValueVectorBuilder aggregator = new DayPrincipalComponentValueVectorBuilder();
		
		Map<DayEigenvalue, List<Double>> map = aggregator.organizePcValByDayIndex(list);
		
		List<DayPrincipalComponentValueVector> result = aggregator.aggregateByDay(map);
		assertNotNull(result);
		assertEquals(2, result.size());
		
		Collections.sort(result, new Comparator<DayPrincipalComponentValueVector>() {
			@Override
			public int compare(DayPrincipalComponentValueVector o1, DayPrincipalComponentValueVector o2) {
				return o1.dayIndex - o2.dayIndex;
			}
		});
		
		DayPrincipalComponentValueVector dpcvv = result.get(0);
		assertEquals((int)day1.getDayIndex(), dpcvv.dayIndex);
		assertEquals(1, dpcvv.prinCompValueVect.getMaxIndex());
		assertEquals((1.1+2.2)/2.0, dpcvv.prinCompValueVect.getEntry(0), eps);
		assertEquals((3.3+4.4+5.5)/3.0, dpcvv.prinCompValueVect.getEntry(1), eps);
		
		dpcvv = result.get(1);
		assertEquals((int)day2.getDayIndex(), dpcvv.dayIndex);
		assertEquals(1, dpcvv.prinCompValueVect.getMaxIndex());
		assertEquals(6.6, dpcvv.prinCompValueVect.getEntry(0), eps);
		assertEquals((7.7+8.8)/2.0, dpcvv.prinCompValueVect.getEntry(1), eps);
	}
}
