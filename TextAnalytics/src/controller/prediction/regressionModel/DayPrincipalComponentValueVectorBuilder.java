package controller.prediction.regressionModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import math.linearAlgebra.DenseFixedVector;
import math.linearAlgebra.Vector;

import org.hibernate.Query;

import controller.util.MapBuilder;
import controller.util.Utilities;
import controller.util.ValueOperator;

import orm.ArticlePcValue;
import orm.Eigenvalue;
import orm.SessionManager;

public class DayPrincipalComponentValueVectorBuilder {
	
	private final MapBuilder mapBuilder;
	
	public DayPrincipalComponentValueVectorBuilder() {
		mapBuilder = new MapBuilder();
	}

	public List<DayPrincipalComponentValueVector> build(int scoringModelId, int minDayIndex) {
		
		Map<DayEigenvalue, List<Double>> map = organizePcValByDayIndex(retrieveArticlePcValues(scoringModelId, minDayIndex));
		
		return aggregateByDay(map);
	}
	
	
	List<DayPrincipalComponentValueVector> aggregateByDay(Map<DayEigenvalue, List<Double>> map) {
		
		class Pair {
			final DayEigenvalue dayEigenvalue;
			final double average;
			public Pair(DayEigenvalue dayEigenvalue, double average) {
				this.dayEigenvalue = dayEigenvalue;
				this.average = average;
			}
		}
		
		//for determining the number of eigenvectors
		Set<Eigenvalue> eigSet = new HashSet<>();
		
		//convert the provided map into a list of key-value pairs, calculating the average of the list in map-value
		List<Pair> pairList = new ArrayList<>(map.size());
		for (DayEigenvalue dayEigenvalue : map.keySet()) {
			eigSet.add(dayEigenvalue.eig);
			
			List<Double> valueList = map.get(dayEigenvalue);
			
			double sum = 0.0;
			for (Double value : valueList) {
				sum += value;
			}

			pairList.add(new Pair(dayEigenvalue, sum/valueList.size()));
		}
		
		final int numEig = eigSet.size();
		eigSet = null;
		
		ValueOperator<Pair, Integer, Vector> vo = new ValueOperator<Pair, Integer, Vector>() {
			@Override
			public Integer getKey(Pair t) {
				return t.dayEigenvalue.dayIndex;
			}
			@Override
			public Vector buildNew() {
				return new DenseFixedVector(numEig);
			}
			@Override
			public void addValue(Pair t, Vector aggregation) {
				aggregation.setEntry(t.dayEigenvalue.eig.getSortIndex(), t.average);
			}
		};
		
		Map<Integer, Vector> dayIndexVectorMap = mapBuilder.build(vo, pairList);
		
		List<DayPrincipalComponentValueVector> result = new ArrayList<>(dayIndexVectorMap.size());
		for (Integer dayIndex : dayIndexVectorMap.keySet()) {
			result.add(new DayPrincipalComponentValueVector(dayIndex, dayIndexVectorMap.get(dayIndex)));
		}
		
		return result;
	}
	
	
	Map<DayEigenvalue, List<Double>> organizePcValByDayIndex(List<ArticlePcValue> artPcValList) {
		ValueOperator<ArticlePcValue, DayEigenvalue, List<Double>> vo = new 
				ValueOperator<ArticlePcValue, DayPrincipalComponentValueVectorBuilder.DayEigenvalue, List<Double>>() {
			@Override
			public DayEigenvalue getKey(ArticlePcValue t) {
				return new DayEigenvalue(t.getArticle().getDayIndex(), t.getEigenvalue());
			}
			@Override
			public List<Double> buildNew() {
				return new LinkedList<>();
			}
			@Override
			public void addValue(ArticlePcValue t, List<Double> aggregation) {
				aggregation.add(t.getValue());
			}
		};
		
		return mapBuilder.build(vo, artPcValList);
	}
	
	
	
	static List<ArticlePcValue> retrieveArticlePcValues(int scoringModelId, int minDayIndex) {
		Query query = SessionManager.createQuery("from ArticlePcValue where eigenvalue.scoringModel.id = :smId" +
				" and article.dayIndex >= :minDayIndex");
		query.setInteger("smId", scoringModelId);
		query.setInteger("minDayIndex", minDayIndex);
		
		return Utilities.convertGenericList(query.list());
	}
	
	static class DayEigenvalue {
		public final int dayIndex;
		public final Eigenvalue eig;
		public DayEigenvalue(int dayIndex, Eigenvalue eig) {
			this.dayIndex = dayIndex;
			this.eig = eig;
		}

		@Override
		public int hashCode() {
			return (31*dayIndex) + eig.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (null == obj) {
				return false;
			}
			if (! (obj instanceof DayEigenvalue)) {
				return false;
			}
			
			DayEigenvalue other = (DayEigenvalue)obj;
			
			return (dayIndex == other.dayIndex) && (eig == other.eig);
		}
	}
}
