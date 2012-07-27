package com.ssl.curriculum.math.task;

import android.os.AsyncTask;

import com.ssl.curriculum.math.logic.PageFlipper;
import com.ssl.curriculum.math.model.activity.ActivityData;
import com.ssl.curriculum.math.service.ActivityContentProvider;

public class FetchActivityDataTask extends AsyncTask<Void, Void, ActivityData> {
	private ActivityContentProvider vacp;
	private PageFlipper flipper;
	private int id = 0;
	public FetchActivityDataTask(PageFlipper flipper, ActivityContentProvider provider, int id){
		vacp = provider;
		this.id = id;
		this.flipper = flipper;
	}
	
	@Override
	protected ActivityData doInBackground(Void... arg0) {
		return vacp.fetchActivityById(this.id);
	}

	@Override
	protected void onPostExecute(ActivityData result) {
		this.flipper.onReceivedActivityData(result);
	}
}
