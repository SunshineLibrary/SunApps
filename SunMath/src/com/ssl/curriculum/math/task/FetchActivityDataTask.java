package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.service.ActivityContentProvider;

public class FetchActivityDataTask extends AsyncTask<Void, Void, Void> {
	private ActivityContentProvider contentProvider;
	private ActivityDataReceiver activityDataReceiver;
	private int activityId = 0;
    private int sectionId;

    public FetchActivityDataTask(ActivityContentProvider provider, ActivityDataReceiver activityDataReceiver, int sectionId, int activityId){
        this.activityDataReceiver = activityDataReceiver;
        contentProvider = provider;
        this.activityId = activityId;
        this.sectionId = sectionId;
    }
	
	@Override
	protected Void doInBackground(Void... voids) {
        contentProvider.fetchSectionActivityData(activityDataReceiver, sectionId, activityId);
        return null;
    }
}
