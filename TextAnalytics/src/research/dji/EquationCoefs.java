package research.dji;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import orm.DjiCorr;

public class EquationCoefs {
	private final Map<DjiCorr, Double> coefMap;
	
	private double constantCoef;
	
	private double constant;

	public EquationCoefs(Collection<DjiCorr> coefBases) {
		coefMap = new HashMap<DjiCorr, Double>();
		
		for (DjiCorr coefBase : coefBases) {
			coefMap.put(coefBase, 0.0);
		}
		
		constantCoef = 0.0;
		constant = 0.0;
	}
	
	public Double getCoef(DjiCorr djiCorr) {
		return coefMap.get(djiCorr);
	}
	
	public void setCoef(DjiCorr djiCorr, Double coef) {
		coefMap.put(djiCorr, coef);
	}
	
	public Set<DjiCorr> getDjiCorrSet() {
		return new HashSet<>(coefMap.keySet());
	}
	
	public double getConstantCoef() {
		return constantCoef;
	}
	
	public void setConstantCoef(double constantCoef) {
		this.constantCoef = constantCoef;
	}
	
	public double getConstant() {
		return constant;
	}
	
	public void setConstant(double constant) {
		this.constant = constant;
	}
}
