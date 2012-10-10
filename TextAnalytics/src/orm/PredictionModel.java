package orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="PREDICTION_MODEL")
public class PredictionModel {

	@Id
	private Integer id;
	
	@OneToOne
	@JoinColumn(name="STOCKPRICE_CHANGE_ID")
	private StockPriceChange stockPriceChange;
	
	@Column(name="THRESHOLD_LOWER")
	private Double thresholdLower;
	
	@Column(name="THRESHOLD_UPPER")
	private Double thresholdUpper;
	
	public PredictionModel() {
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public StockPriceChange getStockPriceChange() {
		return stockPriceChange;
	}

	public void setStockPriceChange(StockPriceChange stockPriceChange) {
		this.stockPriceChange = stockPriceChange;
	}

	public Double getThresholdLower() {
		return thresholdLower;
	}

	public void setThresholdLower(Double thresholdLower) {
		this.thresholdLower = thresholdLower;
	}

	public Double getThresholdUpper() {
		return thresholdUpper;
	}

	public void setThresholdUpper(Double tresholdUpper) {
		this.thresholdUpper = tresholdUpper;
	}
}
