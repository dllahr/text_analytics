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
	
	public static int getMaxId(String className) {
		Object maxIdObj = SessionManager.createQuery("select max(id) from " + className).list().get(0);
		
		return maxIdObj != null ? (int)maxIdObj : 0;
	}
}
