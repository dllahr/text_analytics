package orm;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.Query;

@Entity
@Table(name="REGRESSION_MODEL")
public class RegressionModel {
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regressionModelIdSeq")
	@SequenceGenerator(name = "regressionModelIdSeq", sequenceName = "regression_model_id_seq", allocationSize = 1)
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
		builder.append(id).append(Constants.toStringDelimeter).append(scoringModel.getId()).append(Constants.toStringDelimeter);
		builder.append(company.getId()).append(Constants.toStringDelimeter).append(dayOffset).append(Constants.toStringDelimeter);
		builder.append(rExpression.substring(0,10));
		
		return builder.toString();
	}
	
	public static RegressionModel findById(int regressionModelId) {
		Query query = SessionManager.createQuery("from RegressionModel where id = :id");
		query.setInteger("id", regressionModelId);
		
		@SuppressWarnings("rawtypes")
		List list = query.list();
		
		if (list.size() > 0) {
			return (RegressionModel)list.get(0);
		} else {
			return null;
		}
	}
}
