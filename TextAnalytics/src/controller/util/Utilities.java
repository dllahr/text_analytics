package controller.util;

import java.util.ArrayList;
import java.util.List;

import orm.SessionManager;

public class Utilities {
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertGenericList(@SuppressWarnings("rawtypes") List objList) {
		List<T> result = new ArrayList<>(objList.size());
		for (Object obj : objList) {
			result.add((T)obj);
		}
		return result;
	}
	
	public static Integer getMaxId(String className) {
		@SuppressWarnings("rawtypes")
		List objList = SessionManager.createQuery("select max(id) from " + className).list();
		if (objList.size() > 0) {
			return (Integer)objList.get(0);
		} else {
			return null;
		}
	}
}
