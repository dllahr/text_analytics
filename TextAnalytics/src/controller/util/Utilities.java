package controller.util;

import java.util.ArrayList;
import java.util.List;

public class Utilities {
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertGenericList(@SuppressWarnings("rawtypes") List objList) {
		List<T> result = new ArrayList<>(objList.size());
		for (Object obj : objList) {
			result.add((T)obj);
		}
		return result;
	}
}
