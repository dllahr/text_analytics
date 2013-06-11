package controller.util;

/**
 * @author dlahr
 *
 * @param <T> original object type
 * @param <K> type of key to extract from T
 * @param <W> aggregation type (value to put in map)
 */
public interface ValueOperator<T, K, W> {
	public K getKey(T t);
	
	public W buildNew();
	
	public void addValue(T t, W aggregation);
}
