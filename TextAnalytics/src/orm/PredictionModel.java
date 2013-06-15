package orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "PREDICTION_MODEL")
public class PredictionModel {
	
	private final static String delimeter = " "; 

	@Id
	Integer id;
	
	@ManyToOne
	@JoinColumn(name = "REGRESSION_MODEL_ID")
	RegressionModel regressionModel;
	
	@Column(name = "LOWER_THRESHOLD")
	Double lowerThreshold;
	
	@Column(name = "UPPER_THRESHOLD")
	Double upperThreshold;
	
	@Column(name = "PERCENTILE0_VALUE")
	Double percentile0Value;
	
	@Column(name = "PERCENTILE25_VALUE")
	Double percentile25Value;
	
	@Column(name = "PERCENTILE50_VALUE")
	Double percentile50Value;

	@Column(name = "PERCENTILE75_VALUE")
	Double percentile75Value;
	
	@Column(name = "PERCENTILE100_VALUE")
	Double percentile100Value;
	
	public PredictionModel() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RegressionModel getRegressionModel() {
		return regressionModel;
	}

	public void setRegressionModel(RegressionModel regressionModel) {
		this.regressionModel = regressionModel;
	}

	public Double getLowerThreshold() {
		return lowerThreshold;
	}

	public void setLowerThreshold(Double lowerThreshold) {
		this.lowerThreshold = lowerThreshold;
	}

	public Double getUpperThreshold() {
		return upperThreshold;
	}

	public void setUpperThreshold(Double upperThreshold) {
		this.upperThreshold = upperThreshold;
	}

	public Double getPercentile25Value() {
		return percentile25Value;
	}

	public void setPercentile25Value(Double percentile25Value) {
		this.percentile25Value = percentile25Value;
	}

	public Double getPercentile50Value() {
		return percentile50Value;
	}

	public void setPercentile50Value(Double percentile50Value) {
		this.percentile50Value = percentile50Value;
	}

	public Double getPercentile75Value() {
		return percentile75Value;
	}

	public void setPercentile75Value(Double percentile75Value) {
		this.percentile75Value = percentile75Value;
	}

	public Double getPercentile0Value() {
		return percentile0Value;
	}

	public void setPercentile0Value(Double percentile0Value) {
		this.percentile0Value = percentile0Value;
	}

	public Double getPercentile100Value() {
		return percentile100Value;
	}

	public void setPercentile100Value(Double percentile100Value) {
		this.percentile100Value = percentile100Value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id).append(delimeter).append(regressionModel.getId()).append(delimeter);
		builder.append(lowerThreshold).append(delimeter).append(upperThreshold).append(delimeter);
		builder.append(percentile0Value).append(delimeter).append(percentile25Value).append(delimeter);
		builder.append(percentile50Value).append(delimeter).append(percentile75Value).append(delimeter);
		builder.append(percentile100Value);
		
		return builder.toString();
	}
}
