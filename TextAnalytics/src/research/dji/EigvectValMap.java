package research.dji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import orm.Eigenvalue;
import orm.EigenvectorValue;

public class EigvectValMap {
	private final Map<Integer, Map<Eigenvalue, List<EigenvectorValue>>> evvMap;
	
	private final Set<Integer> dayIndexSet;
	
	private final Set<Eigenvalue> eigenvalueSet;
	
	public EigvectValMap(List<EigenvectorValue> evvList) {
		evvMap = new HashMap<Integer, Map<Eigenvalue,List<EigenvectorValue>>>();
		
		dayIndexSet = new HashSet<>();
		eigenvalueSet = new HashSet<>();
		
		for (EigenvectorValue eigenvectorValue : evvList) {
			final int dayIndex = eigenvectorValue.getArticle().getDayIndex();
			dayIndexSet.add(dayIndex);
			
			final Eigenvalue eigval = eigenvectorValue.getEigenvalue();
			eigenvalueSet.add(eigval);
			
			Map<Eigenvalue, List<EigenvectorValue>> map = evvMap.get(dayIndex);
			if (null == map) {
				map = new HashMap<>();
				evvMap.put(dayIndex, map);
			}
			
			List<EigenvectorValue> list = map.get(eigval);
			if (null == list) {
				list = new LinkedList<>();
				map.put(eigval, list);
			}
			
			list.add(eigenvectorValue);
		}
	}

	public List<EigenvectorValue> getEigvectValList(Integer dayIndex, Eigenvalue eigenvalue) {
		return new ArrayList<>(evvMap.get(dayIndex).get(eigenvalue));
	}

	public Set<Integer> getDayIndexSet() {
		return new HashSet<>(dayIndexSet);
	}

	public Set<Eigenvalue> getEigenvalueSet() {
		return new HashSet<>(eigenvalueSet);
	}
	
	
}
