package main;

import java.util.ArrayList;
import java.util.List;

import orm.PredictionModel;
import orm.PredictionModelStockSmoothingCoef;
import orm.SessionManager;

public class MainCreatePredictionModelSmoothingCoefs {

	private static final double[] coef = {0.5, 1.0, 0.5};
	private static final int[] relDay = {-1, 0, 1};
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("enter prediction model ID's as arguments to generate / save coefficients for them");
			return;
		}
		
		System.out.println("building prediction model coefficients for prediciton model id's:");
		List<Integer> pmIdList = new ArrayList<>(args.length);
		for (String a : args) {
			System.out.println(a + " ");

			pmIdList.add(Integer.valueOf(a));
		}
		
		
		for (PredictionModel pm : PredictionModel.findAllById(pmIdList)) {
			System.out.println("found prediction model and building coefs.  id:  " + pm.getId());

			for (int i = 0; i < coef.length; i++) {
				PredictionModelStockSmoothingCoef pmCoef = new PredictionModelStockSmoothingCoef(pm, relDay[i], coef[i]);
				
				SessionManager.persist(pmCoef);
			}
		}
		
		SessionManager.commit();

	}

}
