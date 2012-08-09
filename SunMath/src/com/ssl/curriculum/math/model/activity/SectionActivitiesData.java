package com.ssl.curriculum.math.model.activity;

import java.util.ArrayList;
import java.util.List;

public class SectionActivitiesData {

    private int sectionId;
    private List<SectionActivityData> sectionActivityDataList;

    public SectionActivitiesData(int sectionId) {
        this.sectionId = sectionId;
        sectionActivityDataList = new ArrayList<SectionActivityData>();

    }

    public int getSectionId() {
        return sectionId;
    }

    public void addSectionActivity(SectionActivityData sectionActivityData) {
        sectionActivityDataList.add(sectionActivityData);
    }
}
