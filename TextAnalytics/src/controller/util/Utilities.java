package controller.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utilities {
	private static final long millisPerDay = 1000*60*60*24;
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertGenericList(@SuppressWarnings("rawtypes") List objList) {
		List<T> result = new ArrayList<>(objList.size());
		for (Object obj : objList) {
			result.add((T)obj);
		}
		return result;
	}
	
	public static int calculateDayIndex(Date date) {
		return (int) (date.getTime() / millisPerDay);
	}
	
	public static Date calculateDate(int dayIndex) {
		return new Date(millisPerDay * (dayIndex+1));
	}
}
