package controller.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class MapBuilder {

	/**
	 * aggregate the collection of items of type T into a map of keys of type K and values
	 * of type W.  Use vo extract the key from each T, generate the new W when needed, and extract
	 * value from T to add to aggregation W
	 * 
	 * @param vo
	 * @param coll
	 * @return
	 */
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
