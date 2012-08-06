package com.ssl.curriculum.math.logic;

import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.listener.EdgeReceiver;
import com.ssl.curriculum.math.listener.PageFlipListener;
import com.ssl.curriculum.math.logic.strategy.FetchNextDomainActivityStrategyImpl;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.presenter.FlipperSubViewsBuilder;
import com.ssl.curriculum.math.task.FetchActivityTaskManager;

import java.util.ArrayList;

public class ActivityFlowController implements EdgeReceiver, ActivityDataReceiver, PageFlipListener {
    private ArrayList<DomainActivityData> domainActivityStack = new ArrayList<DomainActivityData>();
    private ArrayList<Edge> edges;

    private FlipperSubViewsBuilder flipperSubViewsBuilder;
    private FetchActivityTaskManager fetchActivityTaskManager;

    private int currentActivityId;
    private int currentSectionId;

    private int currentPosition = -1;
    private FetchNextDomainActivityStrategy fetchNextDomainActivityStrategy;

    public ActivityFlowController(FlipperSubViewsBuilder flipperSubViewsBuilder, FetchActivityTaskManager fetchActivityTaskManager) {
        this.flipperSubViewsBuilder = flipperSubViewsBuilder;
        this.fetchActivityTaskManager = fetchActivityTaskManager;
        initStrategy();
    }

    private void initStrategy() {
        fetchNextDomainActivityStrategy = new FetchNextDomainActivityStrategyImpl();
    }

    public void loadDomainActivityData(int domainSectionId, int domainActivityId) {
        currentActivityId = domainSectionId;
        currentSectionId = domainActivityId;
        loadActivityAndEdges(domainSectionId, domainActivityId);
    }

    private void loadActivityAndEdges(int domainSectionId, int domainActivityId) {
        fetchActivityTaskManager.fetchEdge(this, domainSectionId, domainActivityId);
        fetchActivityTaskManager.fetchDomainActivity(this, currentSectionId, currentActivityId);
    }

    private void fetchActivityFromRemote() {
        DomainActivityData nextActivityData = fetchNextDomainActivityStrategy.getNextDomainActivityData(getCurrentActivityData(), edges);
        loadActivityAndEdges(nextActivityData.sectionId, nextActivityData.activityId);
    }

    private DomainActivityData getCurrentActivityData() {
        return domainActivityStack.get(currentPosition);
    }

    private boolean isLastActivity() {
        return currentPosition == domainActivityStack.size() - 1;
    }

    @Override
    public void onShowPrevious() {
        if(currentPosition == 0) return;
        currentPosition--;
        flipperSubViewsBuilder.buildViewToFlipper(getCurrentActivityData());
    }

    @Override
    public void onShowNext() {
        if (isLastActivity() && fetchNextDomainActivityStrategy.canGoToNextActivity(getCurrentActivityData(), edges)) {
            fetchActivityFromRemote();
            return;
        }
        currentPosition++;
        flipperSubViewsBuilder.buildViewToFlipper(getCurrentActivityData());
    }

    @Override
    public void onReceivedDomainActivity(DomainActivityData dataDomain) {
        domainActivityStack.add(dataDomain);
        currentPosition++;
        flipperSubViewsBuilder.buildViewToFlipper(dataDomain);
    }

    @Override
    public void onReceivedEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }
}
