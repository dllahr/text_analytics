package controller.activity;

import orm.Activity;

public class CompletedActivity {
	private Activity startActivity;
	
	private Activity endActivity;
	
	public CompletedActivity() {
		
	}

	public Activity getStartActivity() {
		return startActivity;
	}

	public void setStartActivity(Activity startActivity) {
		this.startActivity = startActivity;
	}

	public Activity getEndActivity() {
		return endActivity;
	}

	public void setEndActivity(Activity endActivity) {
		this.endActivity = endActivity;
	}
}
