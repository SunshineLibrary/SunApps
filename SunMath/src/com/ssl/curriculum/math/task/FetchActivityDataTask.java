package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.service.ActivityContentProvider;

public class FetchActivityDataTask extends AsyncTask<Void, Void, DomainActivityData> {
	private ActivityContentProvider contentProvider;
	private ActivityDataReceiver activityDataReceiver;
	private int activityId = 0;
    private int sectionId;

    public FetchActivityDataTask(ActivityDataReceiver activityDataReceiver, ActivityContentProvider provider, int activityId, int currentSectionId){
        this.activityDataReceiver = activityDataReceiver;
        contentProvider = provider;
        this.activityId = activityId;
        sectionId = currentSectionId;
    }
	
	@Override
	protected DomainActivityData doInBackground(Void... voids) {
		return contentProvider.fetchActivityById(this.activityId, sectionId);
	}

	@Override
	protected void onPostExecute(DomainActivityData result) {
		this.activityDataReceiver.onReceivedActivityData(result);
	}
}
