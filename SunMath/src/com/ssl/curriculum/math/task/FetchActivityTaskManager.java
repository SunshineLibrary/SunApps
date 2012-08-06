package com.ssl.curriculum.math.task;

import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.listener.EdgeReceiver;
import com.ssl.curriculum.math.service.ActivityContentProvider;
import com.ssl.curriculum.math.service.EdgeContentProvider;

public class FetchActivityTaskManager {

    private EdgeContentProvider edgeContentProvider;
    private ActivityContentProvider activityContentProvider;

    public FetchActivityTaskManager(EdgeContentProvider edgeContentProvider, ActivityContentProvider activityContentProvider) {
        this.edgeContentProvider = edgeContentProvider;
        this.activityContentProvider = activityContentProvider;
    }

    public void fetchEdge(EdgeReceiver edgeReceiver, int fetchedSectionId, int fetchedActivityId) {
        FetchEdgeTask task = new FetchEdgeTask(edgeContentProvider, edgeReceiver, fetchedSectionId, fetchedActivityId);
        task.execute();
    }

    public void fetchDomainActivity(ActivityDataReceiver activityDataReceiver, int fetchedSectionId, int fetchedActivityId) {
        FetchActivityDataTask task = new FetchActivityDataTask(activityContentProvider, activityDataReceiver, fetchedSectionId, fetchedActivityId);
        task.execute();
    }
}
