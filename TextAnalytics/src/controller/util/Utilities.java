package controller.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Utilities {
	
	public static final String dateFormatString = "yyyy-MM-dd";
	
	public static DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
	
	public static final String fileCommentPrefix = "#";
	
	private static final long millisPerDay = 1000*60*60*24;
	
	public static <T> List<T> convertGenericList(@SuppressWarnings("rawtypes") List objList) {
		List<T> result = new ArrayList<>(objList.size());
		
		convertGenericCollection(objList, result);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void convertGenericCollection(@SuppressWarnings("rawtypes") List objList, Collection<T> destination) {
		for (Object obj : objList) {
			destination.add((T)obj);
		}
	}
	
	public static int calculateDayIndex(Date date) {
		return (int) (date.getTime() / millisPerDay);
	}
	
	public static Date calculateDate(int dayIndex) {
		return new Date(millisPerDay * (dayIndex+1));
	}

}
