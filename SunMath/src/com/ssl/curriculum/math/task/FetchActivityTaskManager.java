package com.ssl.curriculum.math.task;

import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.listener.SectionActivityDataReceiver;
import com.ssl.curriculum.math.service.ActivityContentProvider;
import com.ssl.curriculum.math.service.SectionActivityContentProvider;

public class FetchActivityTaskManager {

    private ActivityContentProvider activityContentProvider;
    private SectionActivityContentProvider sectionActivityContentProvider;

    public FetchActivityTaskManager(ActivityContentProvider activityContentProvider, SectionActivityContentProvider sectionActivityContentProvider) {
        this.activityContentProvider = activityContentProvider;
        this.sectionActivityContentProvider = sectionActivityContentProvider;
    }

    public void fetchActivityData(ActivityDataReceiver activityDataReceiver, int fetchedSectionId, int fetchedActivityId) {
        FetchActivityDataTask task = new FetchActivityDataTask(activityContentProvider, activityDataReceiver, fetchedSectionId, fetchedActivityId);
        task.execute();
    }

    public void fetchSectionActivities(SectionActivityDataReceiver sectionActivityDataReceiver, int fetchedSectionId, int fetchedActivityId) {
        FetchSectionActivityDataTask task = new FetchSectionActivityDataTask(sectionActivityContentProvider, sectionActivityDataReceiver, fetchedSectionId, fetchedActivityId);
        task.execute();
    }
}
