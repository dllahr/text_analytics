package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import orm.Company;
import orm.Eigenvalue;
import orm.RegressionModel;
import orm.RegressionModelCoef;
import orm.ScoringModel;
import orm.SessionManager;

public class MainLoadRegressionModel {
	
	private final static String eigPrefix = "eig";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		final int dayOffset = Integer.valueOf(args[0]);
		final int scoringModelId = Integer.valueOf(args[1]);
		final int companyId = Integer.valueOf(args[2]);
		
		final File inputFile = new File(args[3]);
		
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String curLine = reader.readLine();
		
		RegressionModel regressionModel = createRegressionModel(dayOffset, scoringModelId, companyId, curLine);
		
		Map<Integer, Eigenvalue> idEigMap = buildIdEigenvalueMap(scoringModelId);
		
		while ((curLine = reader.readLine()) != null) {
			
			String[] split = curLine.replaceAll("\\s+", " ").split(" ");
			
			double coef = Double.valueOf(split[1]);
			
			Eigenvalue eig = null;
			String entry = split[0].trim().toLowerCase();
			if (entry.substring(0, 3).equals(eigPrefix)) {
				int eigId = Integer.valueOf(entry.substring(3));
				
				eig = idEigMap.get(eigId);
				
				coef = coef / eig.getValue();
			}
			
			RegressionModelCoef rmc = new RegressionModelCoef();
			rmc.setCoef(coef);
			rmc.setEigenvalue(eig);
			rmc.setRegressionModel(regressionModel);
			
			SessionManager.persist(rmc);
		}

		SessionManager.commit();
		
		reader.close();
	}
	
	static Map<Integer, Eigenvalue> buildIdEigenvalueMap(int scoringModelId) {
		Map<Integer, Eigenvalue> result = new HashMap<Integer, Eigenvalue>();
		
		Collection<Eigenvalue> eigColl = Eigenvalue.getEigenvalueByScoringModel(scoringModelId);
		
		for (Eigenvalue eig : eigColl) {
			result.put(eig.getId(), eig);
		}
		
		return result;
	}
	
	static RegressionModel createRegressionModel(int dayOffset, int scoringModelId, int companyId, String rExpression) {
		RegressionModel result = new RegressionModel();

		result.setDayOffset(dayOffset);
		result.setCompany(Company.findById(companyId));
		result.setrExpression(rExpression);
		result.setScoringModel(ScoringModel.getScoringModel(scoringModelId));
		
		SessionManager.persist(result);
		
		result = (RegressionModel)SessionManager.merge(result);
		return result;
	}
}
