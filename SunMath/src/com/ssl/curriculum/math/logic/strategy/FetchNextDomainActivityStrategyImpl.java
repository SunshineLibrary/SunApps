package com.ssl.curriculum.math.logic.strategy;

import android.util.Log;
import com.ssl.curriculum.math.logic.EdgeConditionMatcher;
import com.ssl.curriculum.math.logic.FetchNextDomainActivityStrategy;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.model.activity.SectionActivitiesData;
import com.ssl.curriculum.math.model.activity.SectionActivityData;
import com.ssl.curriculum.math.utils.StringUtils;

import java.util.List;

public class FetchNextDomainActivityStrategyImpl implements FetchNextDomainActivityStrategy {

    private EdgeConditionMatcher edgeConditionMatcher;

    public FetchNextDomainActivityStrategyImpl() {
        edgeConditionMatcher = new EdgeConditionMatcherImpl();
    }

    @Override
    public SectionActivityData findNextSectionActivity(DomainActivityData currentActivityData, List<Edge> edges, SectionActivitiesData sectionActivitiesData) {
        if (edges == null || edges.size() == 0) return fetchFromActivitiesGroup(sectionActivitiesData, sectionActivitiesData.getSectionActivity(currentActivityData.activityId));
        int activityId = currentActivityData.activityId;
        for (Edge edge : edges) {
            if (edge.getFromActivityId() == activityId) {
                if (isCorrectEdge(edge.getCondition(), currentActivityData.getResult())) {
                    return sectionActivitiesData.getSectionActivity(edge.getToActivityId());
                }
            }
        }
        return fetchFromActivitiesGroup(sectionActivitiesData, sectionActivitiesData.getSectionActivity(currentActivityData.activityId));
    }

    private boolean isCorrectEdge(String condition, String result) {
        if(StringUtils.isEmpty(condition)) return true;
        if(StringUtils.isEmpty(result)) return false;
        if (condition.startsWith("(")) {
            return edgeConditionMatcher.isMatchedWithCondition(condition, result);
        }
        return false;
    }

    private SectionActivityData fetchFromActivitiesGroup(SectionActivitiesData sectionActivitiesData, SectionActivityData currentSectionActivity) {
        Log.i("@fetch activities by order. fetch from all activities:", sectionActivitiesData.toString());
        Log.i("@fetch activities by order. fetch from the:", currentSectionActivity.toString());
        SectionActivityData sectionActivityBySequence = sectionActivitiesData.getSectionActivityBySequence(currentSectionActivity.sequence);
        String msg = null;
        if (sectionActivityBySequence != null) {
            msg = sectionActivityBySequence.toString();
        }
        Log.i("@fetch activities by order. to the:", msg);
        return sectionActivityBySequence;
    }

}
