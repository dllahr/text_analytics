package controller.activity;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import orm.Activity;
import orm.SessionManager;

import tools.Data;
import tools.LoadData;

public class LoadActivityData {
	private static final String deleteQueryStr = "delete from Activity";
	
	public void load(File activityFile) throws IOException, ParseException {
		SessionManager.createQuery(deleteQueryStr).executeUpdate();
		
		final Data activityData = loadFromFile(activityFile);
		
		List<Activity> activityList = buildActivity(activityData);
		
		for (Activity activity : activityList) {
			SessionManager.persist(activity);
		}
		
		SessionManager.commit();
	}
	
	protected static List<Activity> buildActivity(Data activityData) throws ParseException {
		List<Activity> activityList = new ArrayList<>(activityData.getFirstDimension());
		
		ActivityBuilder activityBuilder = new ActivityBuilder();
		
		int id = activityData.getFirstDimension();
		
		for (String[] activityRow : activityData) {
			Activity activity = activityBuilder.build(activityRow);
			activity.setId(id--);
			
			activityList.add(activity);
		}
		
		return activityList;
	}
	
	protected static Data loadFromFile(File activityFile) throws IOException {
		final LoadData loadData = new LoadData();
		
		return loadData.loadCsvWithHeaderRow(activityFile);
	}
}
