package controller.prediction.principalComponent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Query;

import orm.MeanStemCount;
import orm.SessionManager;
import controller.util.Utilities;

public class MeanStemVectorBuilder {
	
	public MeanStemCountVector build(int scoringModelId) {
		List<MeanStemCount> meanStemCountList = retrieveMeanStemCounts(scoringModelId);
		
		double[] vector = buildMeanVector(meanStemCountList);
		
		final int minStemId = getMinStemId(meanStemCountList);
		
		return new MeanStemCountVector(vector, minStemId);
	}
	
	double[] buildMeanVector(List<MeanStemCount> meanStemCountList) {
		double[] result = new double[meanStemCountList.size()];
		
		int index = 0;
		for (MeanStemCount msc : meanStemCountList) {
			result[index] = msc.getValue();
			
			index++;
		}
		
		return result;
	}
	
	List<MeanStemCount> retrieveMeanStemCounts(int scoringModelId) {
		Query query = SessionManager.createQuery("from MeanStemCount where scoringModel.id = :smId order by stem.id");
		query.setInteger("smId", scoringModelId);
		
		return Utilities.convertGenericList(query.list());
	}
	
	int getMinStemId(List<MeanStemCount> meanStemCountList) {
		MeanStemCount minStemId = Collections.min(meanStemCountList, new Comparator<MeanStemCount>() {
			@Override
			public int compare(MeanStemCount o1, MeanStemCount o2) {
				return o1.getStem().getId() - o2.getStem().getId();
			}
		});
		
		return minStemId.getStem().getId();
	}
}
