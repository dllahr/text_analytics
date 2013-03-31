package orm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Article {
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

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
}
