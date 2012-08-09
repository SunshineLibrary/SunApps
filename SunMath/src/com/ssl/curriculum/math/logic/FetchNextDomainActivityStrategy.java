package com.ssl.curriculum.math.logic;

import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;

import java.util.ArrayList;
import java.util.List;

public interface FetchNextDomainActivityStrategy {

    public DomainActivityData getNextDomainActivityData(DomainActivityData currentDomainActivity, ArrayList<Edge> edges);

    public boolean canGoToNextActivity(DomainActivityData currentActivityData, List<Edge> edges) ;
}
