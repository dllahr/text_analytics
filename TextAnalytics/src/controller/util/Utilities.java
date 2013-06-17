package controller.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import orm.Constants;

public class Utilities {
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertGenericList(@SuppressWarnings("rawtypes") List objList) {
		List<T> result = new ArrayList<>(objList.size());
		for (Object obj : objList) {
			result.add((T)obj);
		}
		return result;
	}
	
	public static int calculateDayIndex(Date date) {
		return (int) (date.getTime() / Constants.millisPerDay);
	}
	
	public static Date calculateDate(int dayIndex) {
		return new Date(Constants.millisPerDay * dayIndex);
	}
}
