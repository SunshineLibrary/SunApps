package com.ssl.curriculum.math.listener;

import com.ssl.curriculum.math.model.activity.SectionActivitiesData;

public interface SectionActivityDataReceiver extends ActivityDataReceiver{

    void onReceivedSectionActivities(SectionActivitiesData sectionActivitiesData);

}
