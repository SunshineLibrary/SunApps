package com.ssl.curriculum.math.model.activity;

import java.util.ArrayList;
import java.util.List;

public class SectionActivitiesData {

    private List<SectionActivityData> sectionActivityDataList;

    public SectionActivitiesData() {
        sectionActivityDataList = new ArrayList<SectionActivityData>();
    }

    public void addSectionActivity(SectionActivityData sectionActivityData) {
        sectionActivityDataList.add(sectionActivityData);
    }

    public SectionActivityData getSectionActivity(int toActivityId) {
        for (SectionActivityData sectionActivityData : sectionActivityDataList) {
            if (sectionActivityData.activityId == toActivityId) {
                return sectionActivityData;
            }
        }
        return null;
    }

    public SectionActivityData getSectionActivityBySequence(int sequence) {
        int minSequenceId = -1;
        for (SectionActivityData sectionActivityData : sectionActivityDataList) {
            if (sectionActivityData.sequence > sequence) {
                if (minSequenceId == -1) {
                    minSequenceId = sectionActivityData.activityId;
                    continue;
                }
                if (sectionActivityData.sequence < minSequenceId) {
                    minSequenceId = sectionActivityData.activityId;
                }
            }
        }

        if(minSequenceId == -1) return null;
        return getSectionActivity(minSequenceId);
    }
}
