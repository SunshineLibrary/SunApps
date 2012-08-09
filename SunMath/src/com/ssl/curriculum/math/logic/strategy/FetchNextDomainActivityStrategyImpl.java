package com.ssl.curriculum.math.logic.strategy;

import com.ssl.curriculum.math.logic.FetchNextDomainActivityStrategy;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;

import java.util.ArrayList;
import java.util.List;

public class FetchNextDomainActivityStrategyImpl implements FetchNextDomainActivityStrategy{
    @Override
    public DomainActivityData getNextDomainActivityData(DomainActivityData currentDomainActivity, ArrayList<Edge> edges) {
        return new DomainActivityData();
    }

    @Override
    public boolean canGoToNextActivity(DomainActivityData currentActivityData, List<Edge> edges) {
        return true;
    }
}
