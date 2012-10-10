package controller.activity;

public class InventoryItem {
	private final String description;

	private int quantity;
	
	private double netAmount;
	
	public InventoryItem(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getNetAmount() {
		return netAmount;
	}
	
	public void add(int quantity, double netAmount) {
		this.quantity += quantity;
		this.netAmount += netAmount;
	}
	
	public double calculatedNetPricePerUnit() {
		return netAmount / ((double)quantity);
	}
	
	/**
	 * @param quantity
	 * @return netAmount value of the quantity subtracted
	 */
	public double subtract(int quantity) {
		final double result = netAmount*((double)quantity)/((double)this.quantity);
		
		this.quantity += quantity;
		
		netAmount += result;
		
		return result;
	}
}
