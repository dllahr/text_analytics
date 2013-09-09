package controller.util;

import java.util.Collection;
import java.util.List;

public interface GenericRetriever<T> {
	@SuppressWarnings("rawtypes")
	public List retrieve(Collection<T> coll);
}
