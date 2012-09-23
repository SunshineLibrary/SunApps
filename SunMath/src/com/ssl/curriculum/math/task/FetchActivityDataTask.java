package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.service.ActivityLoader;

public class FetchActivityDataTask extends AsyncTask<Void, Void, Void> {
    private ActivityLoader provider;
    private ActivityDataReceiver activityDataReceiver;
    private int activityId;
    private int sectionId;

    public FetchActivityDataTask(ActivityLoader activityLoader, ActivityDataReceiver activityDataReceiver, int sectionId, int activityId) {
        this.provider = activityLoader;
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
