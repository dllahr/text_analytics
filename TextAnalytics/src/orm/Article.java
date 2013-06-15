package orm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Article {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "articleIdSeq")
	@SequenceGenerator(name = "articleIdSeq", sequenceName = "article_id_seq", allocationSize = 1)
	@Id
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="SCORING_MODEL_ID")
	private ScoringModel scoringModel;
	
	private String filename;
	
	@Column(name="PUBLISH_DATE")
	private Date publishDate;
	
	@Column(name="DAY_INDEX")
	private Integer dayIndex;
	
	@Column(name="START_LINE_NUM")
	private Integer startLineNum;
	
	@Column(name="ADDITIONAL_IDENTIFIER")
	private String additionalIdentifier;
	
	public Article() {
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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Integer getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(Integer dayIndex) {
		this.dayIndex = dayIndex;
	}
	
	public void setDayIndex(Date date) {
		this.dayIndex = calculateDayIndex(date);
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	public static int calculateDayIndex(Date date) {
		return (int) (date.getTime() / Constants.millisPerDay);
	}

	public Integer getStartLineNum() {
		return startLineNum;
	}

	public void setStartLineNum(Integer startLineNum) {
		this.startLineNum = startLineNum;
	}

	public String getAdditionalIdentifier() {
		return additionalIdentifier;
	}

	public void setAdditionalIdentifier(String additionalIdentifier) {
		this.additionalIdentifier = additionalIdentifier;
	}
}
