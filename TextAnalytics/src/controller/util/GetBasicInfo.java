package controller.util;

import java.util.List;

import org.hibernate.Query;


import orm.ScoringModel;
import orm.SessionManager;

public class GetBasicInfo {
	public static List<ScoringModel> getAllCompanies() {
		Query query = SessionManager.createQuery("from Company order by stockSymbol");
		return Utilities.convertGenericList(query.list());
	}
}
