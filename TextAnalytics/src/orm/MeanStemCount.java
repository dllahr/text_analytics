package orm;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="MEAN_STEM_COUNT")
public class MeanStemCount implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "SCORING_MODEL_ID")
	private ScoringModel scoringModel;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "STEM_ID")
	private Stem stem;
	
	private double value;
	
	public MeanStemCount() {
	}

	public MeanStemCount(ScoringModel scoringModel, Stem stem,
			double value) {
		this.scoringModel = scoringModel;
		this.stem = stem;
		this.value = value;
	}

	public ScoringModel getScoringModel() {
		return scoringModel;
	}

	public void setScoringModel(ScoringModel scoringModel) {
		this.scoringModel = scoringModel;
	}

	public Stem getStem() {
		return stem;
	}

	public void setStem(Stem stem) {
		this.stem = stem;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
