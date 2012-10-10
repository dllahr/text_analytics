package research.correlation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class CorrData {

	private Map<Integer, Map<Integer, SumData>> eigDayCorrMap;
	
	CorrData() {
		eigDayCorrMap = new HashMap<Integer, Map<Integer, SumData>>();
	}
	
	void addToCorr(int eigId, int dayOffset, double value) {
		Map<Integer, SumData> dayCorrMap = eigDayCorrMap.get(eigId);
		
		if (null == dayCorrMap) {
			dayCorrMap = new HashMap<>();
			eigDayCorrMap.put(eigId, dayCorrMap);
		}
		
		SumData corr = dayCorrMap.get(dayOffset);
		if (null == corr) {
			corr = new SumData();
			dayCorrMap.put(dayOffset, corr);
		}
		
		corr.value += value;
		corr.count++;
	}

	SumData getCorr(int eigId, int dayOffset) {
		Map<Integer, SumData> dayValueMap = eigDayCorrMap.get(eigId);
		if (null == dayValueMap) {
			return null;
		} else {
			return dayValueMap.get(dayOffset);
		}
	}
	
	Set<Integer> getEigIdSet() {
		return new HashSet<>(eigDayCorrMap.keySet());
	}
	
	Set<Integer> getDayOffsetSet(int eigId) {
		Map<Integer, SumData> dayValueMap = eigDayCorrMap.get(eigId);
		
		final Set<Integer> dayOffsetSet;
		if (null == dayValueMap) {
			dayOffsetSet = new HashSet<>();
		} else {
			dayOffsetSet = new HashSet<>(dayValueMap.keySet());
		}
		
		return dayOffsetSet;
	}
}
