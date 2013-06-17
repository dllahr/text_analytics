package orm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Query;


@Entity
@Table(name = "PREDICTION_MODEL")
public class PredictionModel {

	@Id
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "REGRESSION_MODEL_ID")
	private RegressionModel regressionModel;
	
	@Column(name = "LOWER_THRESHOLD")
	private Double lowerThreshold;
	
	@Column(name = "UPPER_THRESHOLD")
	private Double upperThreshold;
	
	@Column(name = "PERCENTILE0_VALUE")
	private Double percentile0Value;
	
	@Column(name = "PERCENTILE25_VALUE")
	private Double percentile25Value;
	
	@Column(name = "PERCENTILE50_VALUE")
	private Double percentile50Value;

	@Column(name = "PERCENTILE75_VALUE")
	private Double percentile75Value;
	
	@Column(name = "PERCENTILE100_VALUE")
	private Double percentile100Value;
	
	@OneToMany(mappedBy="predictionModel")
	private Set<PredictionModelStockSmoothingCoef> stockSmoothingCoefSet;

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
		builder.append(id).append(Constants.toStringDelimeter).append(regressionModel.getId()).append(Constants.toStringDelimeter);
		builder.append(lowerThreshold).append(Constants.toStringDelimeter).append(upperThreshold).append(Constants.toStringDelimeter);
		builder.append(percentile0Value).append(Constants.toStringDelimeter).append(percentile25Value).append(Constants.toStringDelimeter);
		builder.append(percentile50Value).append(Constants.toStringDelimeter).append(percentile75Value).append(Constants.toStringDelimeter);
		builder.append(percentile100Value);
		
		return builder.toString();
	}

	public Set<PredictionModelStockSmoothingCoef> getStockSmoothingCoefSet() {
		return stockSmoothingCoefSet;
	}

	public void setStockSmoothingCoefSet(
			Set<PredictionModelStockSmoothingCoef> stockSmoothingCoefSet) {
		this.stockSmoothingCoefSet = stockSmoothingCoefSet;
	}
	
	public List<PredictionModelStockSmoothingCoef> buildRelDayIndexSortedList() {
		List<PredictionModelStockSmoothingCoef> result = new ArrayList<>(stockSmoothingCoefSet);
		
		Collections.sort(result, PredictionModelStockSmoothingCoef.buildDayIndexComparator());
		
		return result;
	}
	
	public Filter buildFilter() {
		Filter filter;
		
		if (lowerThreshold != null && upperThreshold != null) {
			filter = new Filter() {
				@Override
				public boolean passesFilter(double value) {
					return value >= lowerThreshold && value <= upperThreshold;
				}
			};
		} else if (lowerThreshold != null) {
			filter = new Filter() {
				@Override
				public boolean passesFilter(double value) {
					return value >= lowerThreshold;
				}
			};
		} else if (upperThreshold != null) {
			filter = new Filter() {
				@Override
				public boolean passesFilter(double value) {
					return value <= upperThreshold;
				}
			};
		} else {
			throw new RuntimeException("PredictionBuilder filterRawPredictions attempting to filter raw predictions but prediction model " +
					"had no thresholds.  prediction model id:  " + id);
		}
		
		return filter;
	}
	
	public interface Filter {
		public boolean passesFilter(double value);
	}
	
	public static PredictionModel findById(int predictionModelId) {
		Query query = SessionManager.createQuery("from PredictionModel where id = :id");
		query.setInteger("id", predictionModelId);
		
		@SuppressWarnings("rawtypes")
		List list = query.list();
		
		if (list.size() > 0) {
			return (PredictionModel)list.get(0);
		} else {
			return null;
		}
				
	}
}
