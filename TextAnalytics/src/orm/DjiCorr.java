package orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="DJI_CORR")
public class DjiCorr implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DAY_OFFSET")
	private Integer dayOffset;
	
	@Id
	@ManyToOne
	@JoinColumn(name="EIGENVALUE_ID")
	private Eigenvalue eigenvalue;
	
	private Double correlation;
	
	private Integer count;
	
	@Column(name="AVE_CORR")
	private Double averageCorrelation;
	
	public DjiCorr() {
		
	}

	public Integer getDayOffset() {
		return dayOffset;
	}

	public void setDayOffset(Integer dayOffset) {
		this.dayOffset = dayOffset;
	}

	public Eigenvalue getEigenvalue() {
		return eigenvalue;
	}

	public void setEigenvalue(Eigenvalue eigenvalueId) {
		this.eigenvalue = eigenvalueId;
	}

	public Double getCorrelation() {
		return correlation;
	}

	public void setCorrelation(Double correlation) {
		this.correlation = correlation;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getAverageCorrelation() {
		return averageCorrelation;
	}

	public void setAverageCorrelation(Double averageCorrelation) {
		this.averageCorrelation = averageCorrelation;
	}

}
