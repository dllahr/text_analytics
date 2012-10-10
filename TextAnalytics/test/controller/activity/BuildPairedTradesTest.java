package controller.activity;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import orm.Activity;
import orm.SessionManager;

public class BuildPairedTradesTest {

	@Test
	public void testGetActivities() {
		BuildPairedTrades builder = new BuildPairedTrades();
		builder.build();
		SessionManager.closeAll();
	}
}
