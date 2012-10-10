package orm;

import java.util.List;

import org.hibernate.Query;

import controller.util.Utilities;

public class UtilitiesForTesting {

	public static <T> List<T> getFirst10Entities(String className) {
		SessionManager.setUseForTest(true);
		
		return getFirst10EntitiesBase(className);
	}
	
	public static <T> List<T> getFirst10EntititiesNoSetDb(String className) {
		return getFirst10EntitiesBase(className);
	}
	
	private static <T> List<T> getFirst10EntitiesBase(String className) {
		Query query = SessionManager.createQuery("from " + className);
		query.setFirstResult(0);
		query.setMaxResults(10);
		
		return Utilities.convertGenericList(query.list());
	}
}
