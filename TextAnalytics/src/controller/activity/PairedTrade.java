package controller.activity;

import java.util.Date;

public class PairedTrade {
	private final String description;
	private final Date closeDate;
	private final int quantity;
	private final double cost;
	private final double saleAmount;
	
	public PairedTrade(String description, Date closeDate, int quantity,
			double cost, double saleAmount) {
		this.description = description;
		this.closeDate = closeDate;
		this.quantity = quantity;
		this.cost = cost;
		this.saleAmount = saleAmount;
	}

	public String getDescription() {
		return description;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getCost() {
		return cost;
	}

	public double getSaleAmount() {
		return saleAmount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(description).append(",");
		builder.append(closeDate).append(",");
		builder.append(quantity).append(",");
		builder.append(cost).append(",");
		builder.append(saleAmount);
		return builder.toString();
	}
	
	
}
