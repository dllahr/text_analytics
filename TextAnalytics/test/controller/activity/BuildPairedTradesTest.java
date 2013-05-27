package controller.activity;

import static org.junit.Assert.*;

import org.junit.Test;

import orm.SessionManager;

public class BuildPairedTradesTest {

	@Test
	public void testGetActivities() {
		BuildPairedTrades builder = new BuildPairedTrades();
		builder.build();
		SessionManager.closeAll();
		
		assertTrue(true);
	}
}
