package research.dji;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.DjiCorr;
import orm.DjiFracChange;
import orm.Eigenvalue;
import orm.EigenvectorValue;
import orm.SessionManager;

public class Regression {
	
	private static final String corrQueryStr = "from DjiCorr order by abs(averageCorrelation) desc";
	private static final int numCorrForRegression = 1;
	
	private static final String djiFracChangeQueryStr = "from DjiFracChange";
	
	private static final String dayIndexParam = "dayIndex";
	private static final String eigenvalueParam = "eigenvalue";
	private static final String eigenvectorValueQueryStr = "from EigenvectorValue where article.dayIndex in (:" 
			+ ") and eigenvalue in (:" + eigenvalueParam + ")";
	
	public static void main(String[] args) {
		System.out.println("starting linear regression model");
		
		//list of eigenvector-day offset combinations to use as basis of calculation
		List<DjiCorr> corrList = getDjiCorr();

		//data to be modeled
		List<DjiFracChange> fracChangeList = getDjiFracChange();
		
		//eigenvector values representing principal component scores of articles
		EigvectValMap evvMap = getEigenvectorValues(corrList, fracChangeList);
		
		//equation coefficients are based on each corr, the constant epsilon, and the constant (b)
		EquationCoefs[] equationCoefsArray = initializeEquations(corrList);
		
		//iterate over each data point to be modeled
		for (DjiFracChange fracChange : fracChangeList) {
			final int fracChangeDay = fracChange.getDayIndex();
			
			double[] baseValue = new double[corrList.size()];
			int i = 0;
			//iterate over each corr as its place within an equation
			for (DjiCorr eqnCorr : corrList) {
				final int dayIndex = fracChangeDay - eqnCorr.getDayOffset();
				
				double sum = 0.0;
				for (EigenvectorValue evv : evvMap.getEigvectValList(dayIndex, eqnCorr.getEigenvalue())) {
					sum += evv.getValue();
				}
				
				baseValue[i] = sum;
				i++;
			}
			
			//iterate over corr as derivative
			int eqnIndex = 0;
			for (DjiCorr derivCorr : corrList) {
				final int dayIndex = fracChangeDay - derivCorr.getDayOffset();
				
				double sum = 0.0;
				for (EigenvectorValue evv : evvMap.getEigvectValList(dayIndex, derivCorr.getEigenvalue())) {
					sum += evv.getValue();
				}
				
				int coefIndex = 0;
				for (DjiCorr eqnCorr : corrList) {
					final double curVal = equationCoefsArray[eqnIndex].getCoef(eqnCorr);
					equationCoefsArray[eqnIndex].setCoef(eqnCorr, curVal + sum*baseValue[coefIndex]);
					
					coefIndex++;
				}
				
				eqnIndex++;
			}
		}
		
		SessionManager.closeAll();
		System.out.println("fin");
	}
	
	private static EquationCoefs[] initializeEquations(Collection<DjiCorr> djiCorrColl) {
		EquationCoefs[] result = new EquationCoefs[djiCorrColl.size() + 1];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = new EquationCoefs(djiCorrColl);
		}
		
		return result;
	}

	private static EigvectValMap getEigenvectorValues(List<DjiCorr> corrList, List<DjiFracChange> fracChangeList) {
		Set<Eigenvalue> eigenvalueSet = new HashSet<>();
		Set<Integer> dayOffsetSet = new HashSet<>();
		for (DjiCorr corr : corrList) {
			dayOffsetSet.add(corr.getDayOffset());
			
			eigenvalueSet.add(corr.getEigenvalue());
		}

		Set<Integer> dayIndexSet = new HashSet<>();
		for (DjiFracChange fracChange : fracChangeList) {
			for (Integer dayOffset : dayOffsetSet) {
				dayIndexSet.add(fracChange.getDayIndex() - dayOffset);
			}
		}

		Query query = SessionManager.createQuery(eigenvectorValueQueryStr);
		query.setParameterList(dayIndexParam, dayIndexSet);
		query.setParameterList(eigenvalueParam, eigenvalueSet);

		List<EigenvectorValue> evvList = Utilities.convertGenericList(query.list());		

		return new EigvectValMap(evvList);
	}
	
	
	private static List<DjiFracChange> getDjiFracChange() {
		return Utilities.convertGenericList(SessionManager.createQuery(djiFracChangeQueryStr).list());
	}
	
	static List<DjiCorr> getDjiCorr() {
		Query query = SessionManager.createQuery(corrQueryStr);
		query.setFirstResult(0);
		query.setMaxResults(numCorrForRegression);
		
		return Utilities.convertGenericList(query.list());
	}
}
