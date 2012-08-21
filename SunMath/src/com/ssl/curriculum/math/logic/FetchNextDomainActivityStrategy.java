package com.ssl.curriculum.math.logic;

import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.model.activity.SectionActivitiesData;
import com.ssl.curriculum.math.model.activity.SectionActivityData;

import java.util.List;

public interface FetchNextDomainActivityStrategy {

    public SectionActivityData findNextSectionActivity(DomainActivityData currentActivityData, List<Edge> edges, SectionActivitiesData sectionActivitiesData);
}
