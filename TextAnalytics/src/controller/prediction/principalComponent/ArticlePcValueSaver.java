package controller.prediction.principalComponent;

import java.util.List;

import orm.ArticlePcValue;
import orm.Eigenvalue;
import orm.SessionManager;

public class ArticlePcValueSaver {
	public void save(List<ArticlePrincipalComponentValues> artPcValueList) {
		for (ArticlePrincipalComponentValues artPcVal : artPcValueList) {
			for (Eigenvalue e : artPcVal.prinCompValuesMap.keySet()) {
				double value = artPcVal.prinCompValuesMap.get(e);
				
				ArticlePcValue artPcValOrm = new ArticlePcValue(artPcVal.article, e, value);
				SessionManager.persist(artPcValOrm);
			}
		}
		
		System.out.println("Commit article pc values to database");
		SessionManager.commit();
	}
}
