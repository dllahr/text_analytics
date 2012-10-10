package research.dji;

import orm.Eigenvalue;

public class EigDayOffsetCorr {

	private final Eigenvalue eigenvalue;
	
	private final int dayOffset;
	
	private Double correlation;
	
	private int count;

	public EigDayOffsetCorr(Eigenvalue eigenvalue, int dayoffset) {
		this.eigenvalue = eigenvalue;
		this.dayOffset = dayoffset;
		
		correlation = 0.0;
		count = 0;
	}

	public Eigenvalue getEigenvalue() {
		return eigenvalue;
	}

	public int getDayOffset() {
		return dayOffset;
	}

	public Double getCorrelation() {
		return correlation;
	}
	
	public int getCount() { 
		return count;
	}

	public void addToCorrelation(Double additionalCorrelation) {
		correlation += additionalCorrelation;
		count++;
	}
}
