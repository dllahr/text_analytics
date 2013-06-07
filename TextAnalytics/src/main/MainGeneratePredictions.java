package main;

import java.util.List;

import orm.MeanStemCount;
import orm.ScoringModel;
import orm.SessionManager;

public class MainGeneratePredictions {

	public static void main(String[] args) {
		final int scoringModelId = Integer.valueOf(args[0]);
		final int companyId = Integer.valueOf(args[1]);
		
		ScoringModel sm = (ScoringModel)SessionManager.createQuery("from ScoringModel where id = :id").setInteger("id", scoringModelId).list().get(0);
		
//		final double[] meanVector = retrieveMeanVector(sm);
		
	}
	
	private static List<MeanStemCount> retrieveMeanVector(ScoringModel sm) {
		
		return null;
	}
}
