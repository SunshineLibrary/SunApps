package com.ssl.curriculum.math.logic;

import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.listener.EdgeReceiver;
import com.ssl.curriculum.math.listener.PageFlipListener;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.presenter.FlipperSubViewsBuilder;
import com.ssl.curriculum.math.task.FetchActivityTaskManager;

import java.util.ArrayList;

public class ActivityFlowController implements EdgeReceiver, ActivityDataReceiver, PageFlipListener {
    private ArrayList<DomainActivityData> domainActivityStack = new ArrayList<DomainActivityData>();

    private FlipperSubViewsBuilder flipperSubViewsBuilder;
    private FetchActivityTaskManager fetchActivityTaskManager;

    private ArrayList<Edge> edges;

    private int currentActivityId;
    private int currentSectionId;

    private int currentPosition = -1;

    public ActivityFlowController(FlipperSubViewsBuilder flipperSubViewsBuilder, FetchActivityTaskManager fetchActivityTaskManager) {
        this.flipperSubViewsBuilder = flipperSubViewsBuilder;
        this.fetchActivityTaskManager = fetchActivityTaskManager;
    }

    public void loadDomainActivityData(int domainSectionId, int domainActivityId) {
        currentActivityId = domainSectionId;
        currentSectionId = domainActivityId;
        startLoadTask(domainSectionId, domainActivityId);
    }

    private void startLoadTask(int domainSectionId, int domainActivityId) {
        fetchActivityTaskManager.fetchEdge(this, domainSectionId, domainActivityId);
        fetchActivityTaskManager.fetchDomainActivity(this, currentSectionId, currentActivityId);
    }

    @Override
    public void onShowNext() {
        if (isLastActivity() && canGoToNextActivity()) {
            fetchActivityFromRemote();
            return;
        }
        currentPosition++;
        flipperSubViewsBuilder.buildViewToFlipper(domainActivityStack.get(currentPosition));
    }

    private boolean canGoToNextActivity() {
        return true;
    }

    private void fetchActivityFromRemote() {
        DomainActivityData nextActivityData = getNextActivityDataFromEdges();
        fetchActivityTaskManager.fetchDomainActivity(this, nextActivityData.sectionId, nextActivityData.activityId);
        fetchActivityTaskManager.fetchEdge(this, nextActivityData.activityId, nextActivityData.sectionId);
    }

    private DomainActivityData getNextActivityDataFromEdges() {
        // according to the edges and each condition
        return new DomainActivityData(1, 2);
    }

    private boolean isLastActivity() {
        return currentPosition == domainActivityStack.size() - 1;
    }

    @Override
    public void onShowPrevious() {
        if(currentPosition == 0) return;
        currentPosition--;
        flipperSubViewsBuilder.buildViewToFlipper(domainActivityStack.get(currentPosition));
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
