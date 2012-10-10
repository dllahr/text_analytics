package controller.activity;

import static org.junit.Assert.*;

import org.junit.Test;

import orm.SessionManager;

public class BuildCompletedActivitiesTest {

	@Test
	public void test() {
		OLD_BuildCompletedActivities builder = new OLD_BuildCompletedActivities();
		builder.build();
		
		SessionManager.closeAll();
	}

}
