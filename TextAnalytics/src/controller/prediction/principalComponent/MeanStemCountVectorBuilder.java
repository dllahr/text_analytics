package controller.prediction.principalComponent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Query;

import orm.MeanStemCount;
import orm.SessionManager;
import controller.util.Utilities;

class MeanStemCountVectorBuilder {
	
	public MeanStemCountVector build(int scoringModelId) {
		List<MeanStemCount> meanStemCountList = retrieveMeanStemCounts(scoringModelId);
		
		final int minStemId = getMinStemId(meanStemCountList);
		
		double[] vector = buildMeanVector(meanStemCountList, minStemId);
		
		return new MeanStemCountVector(vector, minStemId);
	}
	
	double[] buildMeanVector(List<MeanStemCount> meanStemCountList, int minStemId) {
		
		final int size = getMaxStemId(meanStemCountList) + 1;
		
		double[] result = new double[size];

		for (MeanStemCount msc : meanStemCountList) {
			final int index = msc.getStem().getId() - minStemId;

			result[index] = msc.getValue();
		}
		
		return result;
	}
	
	List<MeanStemCount> retrieveMeanStemCounts(int scoringModelId) {
		Query query = SessionManager.createQuery("from MeanStemCount where scoringModel.id = :smId");
		query.setInteger("smId", scoringModelId);
		
		return Utilities.convertGenericList(query.list());
	}
	
	int getMinStemId(List<MeanStemCount> meanStemCountList) {
		MeanStemCount minStemId = Collections.min(meanStemCountList, new StemIdComparator());
		
		return minStemId.getStem().getId();
	}
	
	int getMaxStemId(List<MeanStemCount> meanStemCountList) {
		MeanStemCount maxStemId = Collections.max(meanStemCountList, new StemIdComparator());
		
		return maxStemId.getStem().getId();
	}

	class StemIdComparator implements Comparator<MeanStemCount> {
		@Override
		public int compare(MeanStemCount o1, MeanStemCount o2) {
			return o1.getStem().getId() - o2.getStem().getId();
		}
	}
}
