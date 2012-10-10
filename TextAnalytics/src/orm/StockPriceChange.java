package orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="STOCKPRICE_CHANGE")
public class StockPriceChange {
	@Id
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="CALC_ID")
	private StockPriceChangeCalculation stockPriceChangeCalculation;
	
	@ManyToOne
	private Eigenvalue eigenvalue;
	
	@Column(name="DAY_OFFSET")
	private Integer dayOffset;
	
	private Double average;
	
	private Double fwhm;
	
	public static final String TRUE_USE_FOR_PREDICTION = "Y";
	public static final String FALSE_USE_FOR_PREDICTION = "N";
	@Column(name="USE_FOR_PREDICTION")
	private String useForPrediction;
	
	public StockPriceChange() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public StockPriceChangeCalculation getStockPriceChangeCalculation() {
		return stockPriceChangeCalculation;
	}

	public void setStockPriceChangeCalculation(
			StockPriceChangeCalculation stockPriceChangeCalculation) {
		this.stockPriceChangeCalculation = stockPriceChangeCalculation;
	}

	public Eigenvalue getEigenvalue() {
		return eigenvalue;
	}

	public void setEigenvalue(Eigenvalue eigenvalue) {
		this.eigenvalue = eigenvalue;
	}

	public Integer getDayOffset() {
		return dayOffset;
	}

	public void setDayOffset(Integer dayOffset) {
		this.dayOffset = dayOffset;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	public Double getFwhm() {
		return fwhm;
	}

	public void setFwhm(Double fwhm) {
		this.fwhm = fwhm;
	}
	
	public Boolean useForPrediction() {
		if (useForPrediction != null) {
			return TRUE_USE_FOR_PREDICTION == useForPrediction;
		} else {
			return null;
		}
	}
	
	public void setUseForPrediction(boolean useForPrediction) {
		this.useForPrediction = useForPrediction ? TRUE_USE_FOR_PREDICTION : FALSE_USE_FOR_PREDICTION;
	}
}
