package orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="REGRESSION_MODEL_COEF")
public class RegressionModelCoef {
	@Id
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="REGRESSION_MODEL_ID")
	private RegressionModel regressionModel;
	
	@Column(name="EIGENVALUE_ID")
	private Integer eigenvalueId;
	
	private Double coef;
	
	public RegressionModelCoef() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEigenvalueId() {
		return eigenvalueId;
	}

	public void setEigenvalueId(Integer eigenvalueId) {
		this.eigenvalueId = eigenvalueId;
	}

	public Double getCoef() {
		return coef;
	}

	public void setCoef(Double coef) {
		this.coef = coef;
	}

	public RegressionModel getRegressionModel() {
		return regressionModel;
	}

	public void setRegressionModel(RegressionModel regressionModel) {
		this.regressionModel = regressionModel;
	}
}
