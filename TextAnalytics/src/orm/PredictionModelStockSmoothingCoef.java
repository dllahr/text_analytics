package orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="prdctn_mdl_stock_smooth_coef")
public class PredictionModelStockSmoothingCoef implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne
	@JoinColumn(name="PREDICTION_MODEL_ID")
	private PredictionModel predictionModel;
	
	@Id
	@Column(name="relative_day_index")
	private int relativeDayIndex;
	
	private double coef;
	
	public PredictionModelStockSmoothingCoef() {
	}

	public PredictionModel getPredictionModel() {
		return predictionModel;
	}

	public void setPredictionModel(PredictionModel predictionModel) {
		this.predictionModel = predictionModel;
	}

	public int getRelativeDayIndex() {
		return relativeDayIndex;
	}

	public void setRelativeDayIndex(int relativeDayIndex) {
		this.relativeDayIndex = relativeDayIndex;
	}

	public double getCoef() {
		return coef;
	}

	public void setCoef(double coef) {
		this.coef = coef;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(predictionModel.getId()).append(Constants.toStringDelimeter).append(relativeDayIndex).append(Constants.toStringDelimeter);
		builder.append(coef);

		return builder.toString();
	}
}
