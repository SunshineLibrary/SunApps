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
        int id = -1;
        int minSequence = -1;
        for (SectionActivityData sectionActivityData : sectionActivityDataList) {
            if (sectionActivityData.sequence > sequence) {
                if (minSequence == -1) {
                    minSequence = sectionActivityData.sequence;
                    id = sectionActivityData.activityId;
                    continue;
                }
                if (sectionActivityData.sequence < minSequence) {
                    minSequence = sectionActivityData.sequence;
                    id = sectionActivityData.activityId;
                }
            }
        }

        if(minSequence == -1) return null;
        return getSectionActivity(id);
    }

    @Override
    public String toString() {
        return "Section Activities--" + sectionActivityDataList.toString();
    }
}
