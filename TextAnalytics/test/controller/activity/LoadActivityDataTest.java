package controller.activity;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import orm.Activity;

import tools.Data;

public class LoadActivityDataTest {
	private static final String testFileUrl = "test/controller/activity/testActivityData.csv";

	@Test
	public void test() throws IOException, ParseException {
		File file = new File(testFileUrl);
		assert file.exists();
				
		Data data = LoadActivityData.loadFromFile(file);
		assert data.getFirstDimension() > 0;
		
		List<Activity> activityList = LoadActivityData.buildActivity(data);
		
		for (Activity activity : activityList) {
			assert (activity.getActivityDate() != null);
			System.out.println(activity.toString());
		}
	}

}
