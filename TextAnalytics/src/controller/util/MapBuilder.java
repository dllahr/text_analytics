package controller.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class MapBuilder {

	public <T, K, W> Map<K, W> build(ValueOperator<T, K, W> vo, Collection<T> coll) {
		Map<K, W> result = new HashMap<K, W>();
		
		for (T t : coll) {
			K key = vo.getKey(t);
			
			W agg = result.get(key);
			if (null == agg) {
				agg = vo.buildNew();

				result.put(key, agg);
			}
			
			vo.addValue(t, agg);
		}
		
		return result;
	}
	
}
