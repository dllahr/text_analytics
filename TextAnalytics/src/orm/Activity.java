package orm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Activity {
	@Id
	private Integer id;
	
	@Column(name="ACTIVITY_DATE")
	private Date activityDate;
	
	private String transaction;
	
	private String description;
	
	private String symbol;
	
	@Column(name="QTY")
	private Integer quantity;
	
	@Column(name="FILL_PRICE")
	private Double fillPrice;
	
	private Double commission;
	
	@Column(name="NET_AMOUNT")
	private Double netAmount;
	
	public Activity() {
	}
	
	/*
	 * copy orig entries into new, except id
	 */
	public Activity(Activity orig) {
		id = orig.id;
		activityDate = orig.activityDate;
		transaction = orig.transaction;
		description = orig.description;
		symbol = orig.symbol;
		quantity = orig.quantity;
		fillPrice = orig.fillPrice;
		commission = orig.commission;
		netAmount = orig.netAmount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getFillPrice() {
		return fillPrice;
	}

	public void setFillPrice(Double fillPrice) {
		this.fillPrice = fillPrice;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(id).append(Constants.toStringDelimeter);
		builder.append(activityDate).append(Constants.toStringDelimeter);
		builder.append(transaction).append(Constants.toStringDelimeter);
		builder.append(description).append(Constants.toStringDelimeter);
		builder.append(symbol).append(Constants.toStringDelimeter);
		builder.append(quantity).append(Constants.toStringDelimeter);
		builder.append(fillPrice).append(Constants.toStringDelimeter);
		builder.append(commission).append(Constants.toStringDelimeter);
		builder.append(netAmount);
		
		return builder.toString();
	}
}
