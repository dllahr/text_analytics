package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import controller.util.Utilities;

import orm.Company;
import orm.Eigenvalue;
import orm.PredictionModel;
import orm.PredictionModelStockSmoothingCoef;
import orm.RegressionModel;
import orm.RegressionModelCoef;
import orm.ScoringModel;
import orm.SessionManager;

public class MainLoadRegressionModel {
	
	private final static String eigPrefix = "eig";
	private final static String pmPrefix = "#predictionModel";
	private final static String whiteSpaceRegex = "\\s+";
	private static final int[] pmIndexes = {0, 1, 2, 4, 5};
	private static final String lowerPrefix = "lower";
	private static final String upperPrefix = "upper";
	
	private static final double[] coef = {0.5, 1.0, 0.5};
	private static final int[] relDay = {-1, 0, 1};
	
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
		
		while ((curLine = reader.readLine()) != null && !curLine.startsWith(pmPrefix)) {
			
			String[] split = curLine.replaceAll(whiteSpaceRegex, " ").split(" ");
			
			double coef = Double.valueOf(split[1]);
			
			Eigenvalue eig = null;
			String entry = split[0].toLowerCase();
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
		
		int pmId = PredictionModel.findMaxId() + 1;
		while ((curLine = reader.readLine()) != null) {
			if (!curLine.startsWith(Utilities.fileCommentPrefix)) {
				PredictionModel pm = new PredictionModel();
				pm.setId(pmId);
				pmId++;
				
				pm.setRegressionModel(regressionModel);
				
				String[] split = curLine.replaceAll(whiteSpaceRegex, " ").split(" ");
				
				double[] percentiles = new double[pmIndexes.length];
				for (int i = 0; i < pmIndexes.length; i++) {
					percentiles[i] = Double.valueOf(split[pmIndexes[i]]);
				}
				
				pm.setPercentile0Value(percentiles[0]);
				pm.setPercentile25Value(percentiles[1]);
				pm.setPercentile50Value(percentiles[2]);
				pm.setPercentile75Value(percentiles[3]);
				pm.setPercentile100Value(percentiles[4]);
				
				curLine = reader.readLine();
				split = curLine.replaceAll(whiteSpaceRegex, " ").split(" ");
				double value = Double.valueOf(split[1]);
				if (split[0].equals(lowerPrefix)) {
					pm.setLowerThreshold(value);
				} else if (split[0].equals(upperPrefix)) {
					pm.setUpperThreshold(value);
				} else {
					throw new RuntimeException("MainLoadRegressionModel main did not recognize threshold prefix");
				}
				
				pm.setStockSmoothingCoefSet(new HashSet<PredictionModelStockSmoothingCoef>());
				
				SessionManager.persist(pm);
				
				for (int i = 0; i < coef.length; i++) {
					PredictionModelStockSmoothingCoef pmCoef = new PredictionModelStockSmoothingCoef(pm, relDay[i], coef[i]);
					pm.getStockSmoothingCoefSet().add(pmCoef);
					
					SessionManager.persist(pmCoef);
				}
				
			}
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
		
//		result = (RegressionModel)SessionManager.merge(result);
		return result;
	}
}
