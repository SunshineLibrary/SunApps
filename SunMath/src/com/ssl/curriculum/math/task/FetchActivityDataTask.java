package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.service.ActivityContentProvider;

public class FetchActivityDataTask extends AsyncTask<Void, Void, Void> {
    private ActivityContentProvider provider;
    private ActivityDataReceiver activityDataReceiver;
    private int activityId;
    private int sectionId;

    public FetchActivityDataTask(ActivityContentProvider activityContentProvider, ActivityDataReceiver activityDataReceiver, int sectionId, int activityId) {
        this.provider = activityContentProvider;
        this.activityDataReceiver = activityDataReceiver;
        this.activityId = activityId;
        this.sectionId = sectionId;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        provider.fetchMatchedActivityData(activityDataReceiver, activityId, sectionId);
        return null;
    }
}
