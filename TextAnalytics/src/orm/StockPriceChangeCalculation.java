package orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="STOCKPRICE_CHANGE_CALC")
public class StockPriceChangeCalculation {
	@Id
	private Integer id;
	
	@ManyToOne
	private Company company;
	
	@Column(name="LOWER_STOCK_DAY_IND")
	private Integer lowerStockDayIndex;
	
	@Column(name="UPPER_STOCK_DAY_IND")
	private Integer upperStockDayIndex;
	
	@Column(name="NUM_SAMPLE")
	private Integer numSamples;
	
	@Column(name="HIST_RANGE_LOWER")
	private Double histogramRangeLower;
	
	@Column(name="HIST_RANGE_UPPER")
	private Double histogramRangeUpper;
	
	@Column(name="NUM_BINS")
	private Integer numBins;
	
	@Column(name="PCT_THRESHOLD_LOWER")
	private Double percentageThresholdLower;
	
	@Column(name="PCT_THRESHOLD_UPPER")
	private Double percentageThresholdUpper;
	
	public StockPriceChangeCalculation() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Integer getLowerStockDayIndex() {
		return lowerStockDayIndex;
	}

	public void setLowerStockDayIndex(Integer lowerStockDayIndex) {
		this.lowerStockDayIndex = lowerStockDayIndex;
	}

	public Integer getUpperStockDayIndex() {
		return upperStockDayIndex;
	}

	public void setUpperStockDayIndex(Integer upperStockDayIndex) {
		this.upperStockDayIndex = upperStockDayIndex;
	}

	public Integer getNumSamples() {
		return numSamples;
	}

	public void setNumSamples(Integer numSamples) {
		this.numSamples = numSamples;
	}

	public Double getHistogramRangeLower() {
		return histogramRangeLower;
	}

	public void setHistogramRangeLower(Double histogramRangeLower) {
		this.histogramRangeLower = histogramRangeLower;
	}

	public Double getHistogramRangeUpper() {
		return histogramRangeUpper;
	}

	public void setHistogramRangeUpper(Double histogramRangeUpper) {
		this.histogramRangeUpper = histogramRangeUpper;
	}

	public Integer getNumBins() {
		return numBins;
	}

	public void setNumBins(Integer numBins) {
		this.numBins = numBins;
	}

	public Double getPercentageThresholdLower() {
		return percentageThresholdLower;
	}

	public void setPercentageThresholdLower(Double percentageThresholdLower) {
		this.percentageThresholdLower = percentageThresholdLower;
	}

	public Double getPercentageThresholdUpper() {
		return percentageThresholdUpper;
	}

	public void setPercentageThresholdUpper(Double percentageThresholdUpper) {
		this.percentageThresholdUpper = percentageThresholdUpper;
	}

}
