package controller.util;

import java.util.List;

import org.hibernate.Query;


import orm.Company;
import orm.SessionManager;

public class GetBasicInfo {
	public static List<Company> getAllCompanies() {
		Query query = SessionManager.createQuery("from Company order by stockSymbol");
		return Utilities.convertGenericList(query.list());
	}
}
