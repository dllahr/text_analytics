package tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TimeDataSeries<T extends Comparable<T>, D> 
implements Iterable<TimeDataPair<T, D>> {
	
	private List<TimeDataPair<T, D>> series;
	
	private Map<T, Integer> timeIndexMap;
	
	public TimeDataSeries(List<TimeDataPair<T,D>> series) {
		this.series = new ArrayList<TimeDataPair<T,D>>(series);

		Comparator<TimeDataPair<T,D>> comp = new Comparator<TimeDataPair<T,D>>() {
			@Override
			public int compare(TimeDataPair<T,D> o1, TimeDataPair<T,D> o2) {
				return o1.getTimeValue().compareTo(o2.getTimeValue());
			}
		};
		
		Collections.sort(this.series, comp);
		
		timeIndexMap = new HashMap<T, Integer>();
		int index = 0;
		for (TimeDataPair<T, D> pair : this.series) {
			if (! timeIndexMap.containsKey(pair.getTimeValue())) {
				timeIndexMap.put(pair.getTimeValue(), index);
			}
			index++;
		}
	}

	@Override
	public Iterator<TimeDataPair<T, D>> iterator() {
		return new ArrayList<TimeDataPair<T,D>>(series).iterator();
	}
	
	public Iterator<TimeDataPair<T, D>> iteratorAtFirstTime(T time) {
		if (timeIndexMap.containsKey(time)) { 
			final int index = timeIndexMap.get(time);
			return new ArrayList<TimeDataPair<T,D>>(series).listIterator(index);
		} else {
			return null;
		}
	}
	
	public TimeDataPair<T, D> getFirstAtTime(T time) {
		Integer index = timeIndexMap.get(time);

		return index != null ? series.get(index) : null;
	}
	
	public int size() {return series.size();}
	
	public TimeDataPair<T, D> get(int index) {return series.get(index);}
}
