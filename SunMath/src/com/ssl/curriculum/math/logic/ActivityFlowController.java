package com.ssl.curriculum.math.logic;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.listener.EdgeReceiver;
import com.ssl.curriculum.math.listener.PageFlipListener;
import com.ssl.curriculum.math.logic.strategy.FetchNextDomainActivityStrategyImpl;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.presenter.FlipperViewsBuilder;
import com.ssl.curriculum.math.task.FetchActivityTaskManager;

import java.util.ArrayList;

public class ActivityFlowController implements EdgeReceiver, ActivityDataReceiver, PageFlipListener {
    private ArrayList<DomainActivityData> domainActivityStack = new ArrayList<DomainActivityData>();
    private ArrayList<Edge> edges;

    private ViewFlipper viewFlipper;
    private FlipperViewsBuilder flipperViewsBuilder;
    private FetchActivityTaskManager fetchActivityTaskManager;

    private int currentActivityId;
    private int currentSectionId;

    private int currentPosition = -1;
    private FetchNextDomainActivityStrategy fetchNextDomainActivityStrategy;

    public ActivityFlowController(ViewFlipper viewFlipper, FlipperViewsBuilder flipperViewsBuilder, FetchActivityTaskManager fetchActivityTaskManager, FetchNextDomainActivityStrategyImpl fetchNextDomainActivityStrategy) {
        this.viewFlipper = viewFlipper;
        this.flipperViewsBuilder = flipperViewsBuilder;
        this.fetchActivityTaskManager = fetchActivityTaskManager;
        this.fetchNextDomainActivityStrategy = fetchNextDomainActivityStrategy;
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
        if (currentPosition == 0) return;
        showPrevious();
    }

    @Override
    public void onShowNext() {
        if (isLastActivity() && fetchNextDomainActivityStrategy.canGoToNextActivity(getCurrentActivityData(), edges)) {
            fetchActivityFromRemote();
            return;
        }
        showNext();
    }

    private void showPrevious() {
        viewFlipper.showPrevious();
        currentPosition--;
    }

    private void showNext() {
        viewFlipper.showNext();
        currentPosition++;
    }

    @Override
    public void onReceivedDomainActivity(DomainActivityData dataDomain) {
        View view = flipperViewsBuilder.buildViewToFlipper(dataDomain);
        if (view == null) return;
        domainActivityStack.add(dataDomain);
        addViewToFlipper(view);
        showNext();
    }

    @Override
    public void onReceivedEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    private void addViewToFlipper(View view) {
        viewFlipper.addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }
}
