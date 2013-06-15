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
	private static final String delimeter = " ";
	
	@Id
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="SCORING_MODEL_ID")
	private ScoringModel scoringModel;
	
	@ManyToOne
	@JoinColumn(name="COMPANY_ID")
	private Company company;
	
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id).append(delimeter).append(scoringModel.getId()).append(delimeter);
		builder.append(company.getId()).append(delimeter).append(dayOffset).append(delimeter);
		builder.append(rExpression.substring(0,10));
		
		return builder.toString();
	}	
}
