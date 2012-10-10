package orm;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="STOCK_DATA")
public class StockData implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final long millisPerDay = 1000*60*60*24;
	
	@Id
	@ManyToOne
	private Company company;
	
	@Id
	@Column(name="DAY_TIME")
	private Date dayTime;
	
	private Double open;
	private Double high;
	private Double low;
	private Double close;
	private Long volume;
	
	@Column(name="ADJ_CLOSE")
	private Double adjustedClose;
	
	@Column(name="DAY_INDEX")
	private Integer dayIndex;
	
	public StockData() {
		
	}
	
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Date getDayTime() {
		return dayTime;
	}

	public void setDayTime(Date dayTime) {
		this.dayTime = dayTime;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public Double getAdjustedClose() {
		return adjustedClose;
	}

	public void setAdjustedClose(Double adjustedClose) {
		this.adjustedClose = adjustedClose;
	}

	public Integer getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(Integer dayIndex) {
		this.dayIndex = dayIndex;
	}
	
	public void setDayTimeAndDayIndex(Date dayTime) {
		setDayTime(dayTime);
		setDayIndex((int)(dayTime.getTime() / millisPerDay));
	}
}
