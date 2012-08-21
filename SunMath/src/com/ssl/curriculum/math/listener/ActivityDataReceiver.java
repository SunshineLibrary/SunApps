package com.ssl.curriculum.math.listener;

import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;

import java.util.List;

public interface ActivityDataReceiver {

    void onReceivedDomainActivity(DomainActivityData ad);

    void onReceivedEdges(List<Edge> edges);
}
