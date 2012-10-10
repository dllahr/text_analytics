package controller.activity;

import static org.junit.Assert.*;

import org.junit.Test;

public class InventoryItemTest {
	private static final double eps = 1e-10;

	@Test
	public void test() {
		InventoryItem invItem = new InventoryItem("description");
		
		invItem.add(10, 100);
		assertEquals(10.0, invItem.calculatedNetPricePerUnit(), eps);
		
		invItem.add(10, 50.0);
		assertEquals(7.5, invItem.calculatedNetPricePerUnit(), eps);
		assertEquals(150.0, invItem.getNetAmount(), eps);
		assertEquals(20, invItem.getQuantity());
		
		final double subtractedAmount = invItem.subtract(5);
		assertEquals(37.5, subtractedAmount, eps);
		assertEquals(112.5, invItem.getNetAmount(), eps);
		assertEquals(15, invItem.getQuantity());
	}

}
