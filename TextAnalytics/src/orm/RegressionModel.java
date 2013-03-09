package orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="REGRESSION_MODEL")
public class RegressionModel {
	
	@Id
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="SCORING_MODEL_ID")
	private ScoringModel scoringModel;
	
	@Column(name="DAY_OFFSET")
	private Integer dayOffset;
	
	@Column(name="R_EXPRESSION")
	private String rExpression;
	
	public RegressionModel() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ScoringModel getScoringModel() {
		return scoringModel;
	}

	public void setScoringModel(ScoringModel scoringModel) {
		this.scoringModel = scoringModel;
	}

	public Integer getDayOffset() {
		return dayOffset;
	}

	public void setDayOffset(Integer dayOffset) {
		this.dayOffset = dayOffset;
	}

	public String getrExpression() {
		return rExpression;
	}

	public void setrExpression(String rExpression) {
		this.rExpression = rExpression;
	}

	@Override
	public String toString() {
		return id + " " + scoringModel.getId() + " " + dayOffset + " " + rExpression.substring(0, 10);
	}
	
	
}
