package controller.util;

import java.util.List;

import org.hibernate.Query;


import orm.ScoringModel;
import orm.SessionManager;

public class GetBasicInfo {
	public static List<ScoringModel> getAllScoringModels() {
		Query query = SessionManager.createQuery("from ScoringModel order by id");
		return Utilities.convertGenericList(query.list());
	}
}
