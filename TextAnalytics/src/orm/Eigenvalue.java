package orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;


@Entity
public class Eigenvalue {
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eigenvalueIdSeq")
	@SequenceGenerator(name = "eigenvalueIdSeq", sequenceName = "eigenvalue_id_seq", allocationSize = 1)
	@Id
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "SCORING_MODEL_ID")
	private ScoringModel scoringModel;
	
	@Column(name="SORT_INDEX")
	private Integer sortIndex;
	
	private Double value;
	
	public Eigenvalue() {
	}

	public Eigenvalue(ScoringModel scoringModel, Integer sortIndex, Double value) {
		this.scoringModel = scoringModel;
		this.sortIndex = sortIndex;
		this.value = value;
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

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
