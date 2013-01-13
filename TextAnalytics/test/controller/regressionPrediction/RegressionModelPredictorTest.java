package controller.regressionPrediction;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.junit.Test;

import controller.util.Utilities;

import orm.Article;
import orm.ArticlePcValue;
import orm.Company;
import orm.Eigenvalue;
import orm.SessionManager;

public class RegressionModelPredictorTest {
	
	@Test
	public void testGetArticlePcValueList() {
		RegressionModelPredictor rmp = new RegressionModelPredictor();
		
		final Company company = (Company)SessionManager.createQuery("from Company where id=3").list().get(0);
		
		final List<Eigenvalue> eigList = Utilities.convertGenericList(SessionManager.createQuery("from Eigenvalue where company.id=3").list());
		final int currentDayIndex = (int)((new Date()).getTime() / (24*60*60*1000));
		
		int[] dayOffsetArray = {10, 20, 30, 40};
		for (int dayOffset : dayOffsetArray) {
			final int startDayIndex = currentDayIndex - dayOffset;
			final List<Integer> dayIndexList = Utilities.convertGenericList(SessionManager.createQuery(
					"select distinct dayIndex from Article where company.id=3 and dayIndex >= " + startDayIndex).list());
			
			List<ArticlePcValue> list = rmp.getArticlePcValueList(company, dayOffset);
			
			Set<Integer> dayIndexSet = new HashSet<>();
			Set<Integer> eigIdSet = new HashSet<>();
			for (ArticlePcValue apv : list) {
				dayIndexSet.add(apv.getArticle().getDayIndex());
				eigIdSet.add(apv.getEigenvalue().getId());
			}
			
			for (Eigenvalue eig : eigList) {
				assertTrue(eigIdSet.remove(eig.getId()));
			}
			assertEquals(0, eigIdSet.size());
			
			for (Integer dayIndex : dayIndexList) {
				assertTrue(dayIndexSet.remove(dayIndex));
			}
			assertEquals(0, dayIndexSet.size());
		}
		
	}

	@Test
	public void testCalcAvePcVal() {
		double[][][] valueArray1 = {{{1.1, 2.2, 3.3}}};
		testUsingValueArray(valueArray1);
		
		double[][][] valueArray2 = {{{1.1, 2.2, 3.3}, {4.4, 5.5, 6.6}}};
		testUsingValueArray(valueArray2);

		double[][][] valueArray3 = {{{1.1, 2.2, 3.3}, {4.4, 5.5, 6.6}},
				{{7.7,8.8,9.9}, {10.10, 11.11, 12.12}}};
		testUsingValueArray(valueArray3);
	}
	
	private static void testUsingValueArray(double[][][] valueArray) {
		RegressionModelPredictor rmp = new RegressionModelPredictor();
		
		ListExpectedArrayPair pair = buildListExpectedArrayPair(valueArray);

		Map<Integer, Map<Integer, SumCountPair>> map = rmp.calculateAveragePcVal(pair.list);

		for (int dayIndex = 0; dayIndex < pair.expectedArray.length; dayIndex++) {
			for (int eigId = 0; eigId < pair.expectedArray[dayIndex].length; eigId++) {
				assertEquals(pair.expectedArray[dayIndex][eigId], map.get(dayIndex).get(eigId).calcAve(), 0.0001);
			}
		}
	}
	
	private static ListExpectedArrayPair buildListExpectedArrayPair(double[][][] valueArray) {
		List<ArticlePcValue> list = new LinkedList<>();
		
		double[][] expectedArray = new double[valueArray.length][valueArray[0].length];
		
		for (int dayIndex = 0; dayIndex < valueArray.length; dayIndex++) {
			for (int eigId = 0; eigId < valueArray[dayIndex].length; eigId++) {
				
				double sum = 0.0;
				for (double value : valueArray[dayIndex][eigId]) {
					list.add(buildArticlePcValue(dayIndex, eigId, value));
					sum += value;
				}
				
				expectedArray[dayIndex][eigId] = sum / ((double)valueArray[dayIndex][eigId].length);
			}
		}
		
		return new ListExpectedArrayPair(list, expectedArray);
	}

	private static ArticlePcValue buildArticlePcValue(int dayIndex, int eigenvalueId, double value) {
		ArticlePcValue apv = new ArticlePcValue();
		apv.setArticle(new Article());
		apv.getArticle().setDayIndex(dayIndex);
		apv.setEigenvalue(new Eigenvalue());
		apv.getEigenvalue().setId(eigenvalueId);
		apv.setValue(value);
		
		return apv;
	}
}
