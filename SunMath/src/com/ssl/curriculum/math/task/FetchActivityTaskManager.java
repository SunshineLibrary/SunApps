package com.ssl.curriculum.math.task;

import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.listener.SectionActivityDataReceiver;
import com.ssl.curriculum.math.service.ActivityLoader;
import com.ssl.curriculum.math.service.SectionActivityLoader;

public class FetchActivityTaskManager {

    private ActivityLoader activityLoader;
    private SectionActivityLoader sectionActivityLoader;

    public FetchActivityTaskManager(ActivityLoader activityLoader, SectionActivityLoader sectionActivityLoader) {
        this.activityLoader = activityLoader;
        this.sectionActivityLoader = sectionActivityLoader;
    }

    public void fetchActivityData(ActivityDataReceiver activityDataReceiver, int fetchedSectionId, int fetchedActivityId) {
        FetchActivityDataTask task = new FetchActivityDataTask(activityLoader, activityDataReceiver, fetchedSectionId, fetchedActivityId);
        task.execute();
    }

    public void fetchSectionActivities(SectionActivityDataReceiver sectionActivityDataReceiver, int fetchedSectionId, int fetchedActivityId) {
        FetchSectionActivityDataTask task = new FetchSectionActivityDataTask(sectionActivityLoader, sectionActivityDataReceiver, fetchedSectionId, fetchedActivityId);
        task.execute();
    }
}
