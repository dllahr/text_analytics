package orm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dji_frac_change")
public class DjiFracChange {
	
	@Id
	@Column(name="DAY_INDEX")
	private Integer dayIndex;
	
	@Column(name="FRAC_CHANGE")
	private Double fracChange;
	
	@Column(name="ADJ_CLOSE")
	private Double adjClose;
	
	@Column(name="DAY_TIME")
	private Date dayTime;
	
	private Long volume;
	
	public DjiFracChange() {
	}

	public Integer getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(Integer dayIndex) {
		this.dayIndex = dayIndex;
	}

	public Double getFracChange() {
		return fracChange;
	}

	public void setFracChange(Double fracChange) {
		this.fracChange = fracChange;
	}

	public Double getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(Double adjClose) {
		this.adjClose = adjClose;
	}

	public Date getDayTime() {
		return dayTime;
	}

	public void setDayTime(Date dayTime) {
		this.dayTime = dayTime;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}
}
