package orm;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Query;

import controller.util.Utilities;

@Entity
@Table(name="REGRESSION_MODEL_COEF")
public class RegressionModelCoef {
	@Id
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="REGRESSION_MODEL_ID")
	private RegressionModel regressionModel;
	
	@ManyToOne
	@JoinColumn(name="EIGENVALUE_ID")
	private Eigenvalue eigenvalue;
	
	private Double coef;
	
	public RegressionModelCoef() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Eigenvalue getEigenvalue() {
		return eigenvalue;
	}

	public void setEigenvalue(Eigenvalue eigenvalue) {
		this.eigenvalue = eigenvalue;
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

	public static List<Integer> retrieveEigenvalueIdsForRegressionModel(int regressionModelId) {
		Query query = SessionManager.createQuery("select eigenvalue.id from RegressionModelCoef where regressionModel.id = :regressionModelId");
		query.setInteger("regressionModelId", regressionModelId);
		
		return Utilities.convertGenericList(query.list());
	}
}
