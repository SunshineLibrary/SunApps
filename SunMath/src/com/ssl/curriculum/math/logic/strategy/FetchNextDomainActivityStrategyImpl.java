package com.ssl.curriculum.math.logic.strategy;

import com.ssl.curriculum.math.logic.FetchNextDomainActivityStrategy;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;

import java.util.ArrayList;

public class FetchNextDomainActivityStrategyImpl implements FetchNextDomainActivityStrategy{
    @Override
    public DomainActivityData getNextDomainActivityData(DomainActivityData currentDomainActivity, ArrayList<Edge> edges) {
        return new DomainActivityData(1, 1);
    }

    @Override
    public boolean canGoToNextActivity(DomainActivityData currentActivityData, ArrayList<Edge> edges) {
        return true;
    }
}
