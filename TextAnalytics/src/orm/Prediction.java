package orm;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Prediction implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name="ARTICLE_ID")
	private Article article;
	
	@Id
	@ManyToOne
	@JoinColumn(name="PREDICTION_MODEL_ID")
	private PredictionModel predictionModel;
	
	@Column(name="STOCK_PRICE")
	private Double stockPrice;
	
	@Column(name="DAY_TIME")
	private Date dayTime;
	
	public Prediction() {
		
	}

	public Prediction(Article article, PredictionModel predictionModel,
			Double stockPrice, Date dayTime) {
		this.article = article;
		this.predictionModel = predictionModel;
		this.stockPrice = stockPrice;
		this.dayTime = dayTime;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public PredictionModel getPredictionModel() {
		return predictionModel;
	}

	public void setPredictionModel(PredictionModel predictionModel) {
		this.predictionModel = predictionModel;
	}

	public Double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(Double stockPrice) {
		this.stockPrice = stockPrice;
	}

	public Date getDayTime() {
		return dayTime;
	}

	public void setDayTime(Date dayTime) {
		this.dayTime = dayTime;
	}
	
	
}
