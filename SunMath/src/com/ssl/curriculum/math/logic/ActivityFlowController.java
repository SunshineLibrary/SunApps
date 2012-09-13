package com.ssl.curriculum.math.logic;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.component.flipperchildren.FlipperChildView;
import com.ssl.curriculum.math.listener.PageFlipListener;
import com.ssl.curriculum.math.listener.SectionActivityDataReceiver;
import com.ssl.curriculum.math.logic.strategy.FetchNextDomainActivityStrategyImpl;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.model.activity.SectionActivitiesData;
import com.ssl.curriculum.math.model.activity.SectionActivityData;
import com.ssl.curriculum.math.presenter.FlipperViewsBuilder;
import com.ssl.curriculum.math.task.FetchActivityTaskManager;
import com.sunshine.metadata.provider.MetadataContract;

import java.util.ArrayList;
import java.util.List;

public class ActivityFlowController implements SectionActivityDataReceiver, PageFlipListener {
    private ArrayList<DomainActivityData> domainActivityStack = new ArrayList<DomainActivityData>();
    private SectionActivitiesData sectionActivitiesData;
    private List<Edge> edges;
    private int sectionId;

    private ViewFlipper viewFlipper;
    private FlipperViewsBuilder flipperViewsBuilder;
    private FetchActivityTaskManager fetchActivityTaskManager;
    private FlipAnimationManager flipAnimationManager;

    private int currentActivityId;
    private int currentPosition = -1;

    private FetchNextDomainActivityStrategy fetchNextDomainActivityStrategy;

    private Animation flipInFromRightAnimation;
    private Animation flipOutToLeftAnimation;
    private Animation flipInFromLeftAnimation;
    private Animation flipOutToRightAnimation;
    private Handler handler;

    public ActivityFlowController(ViewFlipper viewFlipper, FlipperViewsBuilder flipperViewsBuilder, FetchActivityTaskManager fetchActivityTaskManager, FetchNextDomainActivityStrategyImpl fetchNextDomainActivityStrategy, FlipAnimationManager flipAnimationManager) {
        this.viewFlipper = viewFlipper;
        this.flipperViewsBuilder = flipperViewsBuilder;
        this.fetchActivityTaskManager = fetchActivityTaskManager;
        this.fetchNextDomainActivityStrategy = fetchNextDomainActivityStrategy;
        this.flipAnimationManager = flipAnimationManager;
        initAnimation();
        initListeners();
        handler = new Handler();
    }

    private void initListeners() {
        Animation.AnimationListener flipperInAnimationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getCurrentFlippingView().onAfterFlippingIn();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        flipInFromRightAnimation.setAnimationListener(flipperInAnimationListener);
        flipInFromLeftAnimation.setAnimationListener(flipperInAnimationListener);
    }

    private void initAnimation() {
        flipInFromRightAnimation = flipAnimationManager.getFlipInFromRightAnimation();
        flipOutToLeftAnimation = flipAnimationManager.getFlipOutToLeftAnimation();
        flipInFromLeftAnimation = flipAnimationManager.getFlipInFromLeftAnimation();
        flipOutToRightAnimation = flipAnimationManager.getFlipOutToRightAnimation();
    }

    public void loadDomainActivityData(int domainSectionId, int domainActivityId) {
        sectionId = domainSectionId;
        currentActivityId = domainActivityId;
        fetchActivityTaskManager.fetchSectionActivities(this, sectionId, currentActivityId);
    }

    private void fetchActivityFromRemote(int activityId) {
        fetchActivityTaskManager.fetchActivityData(this, sectionId, activityId);
    }

    private DomainActivityData getCurrentActivityData() {
        return domainActivityStack.get(currentPosition);
    }

    private boolean isLastActivity() {
        return domainActivityStack.size() == 0 || currentPosition == domainActivityStack.size() - 1;
    }

    @Override
    public void onShowPrevious() {
        if (currentPosition == 0) return;
        showPrevious();
    }

    @Override
    public void onShowNext() {
        if (!isLastActivity()) {
            showNext();
            return;
        }
        SectionActivityData nextSectionActivity = fetchNextDomainActivityStrategy.findNextSectionActivity(getCurrentActivityData(), edges, sectionActivitiesData);
        if (nextSectionActivity == null) return;
        fetchActivityFromRemote(nextSectionActivity.activityId);
    }

    private void showPrevious() {
        currentPosition--;
        setShowPreviousAnimation();
        onCurrentViewFlipOut();
        viewFlipper.showPrevious();
    }

    private void setShowPreviousAnimation() {
        viewFlipper.setInAnimation(flipInFromRightAnimation);
        viewFlipper.setOutAnimation(flipOutToLeftAnimation);
    }

    private void setShowNextAnimation() {
        viewFlipper.setInAnimation(flipInFromLeftAnimation);
        viewFlipper.setOutAnimation(flipOutToRightAnimation);
    }

    private void showNext() {
        currentPosition++;
        setShowNextAnimation();
        onCurrentViewFlipOut();
        viewFlipper.setDisplayedChild(currentPosition);
    }

    private DomainActivityData getNextDomainActivityData() {
        return domainActivityStack.get((currentPosition + 1) >= domainActivityStack.size() ? currentPosition : currentPosition + 1);
    }

    private DomainActivityData getPreviousDomainActivityData() {
        return domainActivityStack.get((currentPosition - 1) < 0 ? 0 : currentPosition - 1);
    }


    private void onCurrentViewFlipOut() {
        getCurrentFlippingView().onBeforeFlippingOut();
    }

    private FlipperChildView getCurrentFlippingView() {
        return (FlipperChildView) viewFlipper.getChildAt(viewFlipper.getDisplayedChild());
    }

    @Override
    public void onReceivedDomainActivity(final DomainActivityData dataDomain) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                boolean isAdded = flipperViewsBuilder.buildViewToFlipper(viewFlipper, dataDomain);
                if (isAdded) domainActivityStack.add(dataDomain);
                showNext();
            }
        });
    }

    @Override
    public void onReceivedSectionActivities(SectionActivitiesData sectionActivitiesData) {
        this.sectionActivitiesData = sectionActivitiesData;
    }

    @Override
    public void onReceivedEdges(List<Edge> edges) {
        this.edges = edges;
    }

    private void addViewToFlipper(View view) {
        viewFlipper.addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }

    public void destroyFlipperSubViews() {
        for (int index = 0; index < viewFlipper.getChildCount(); index++) {
            FlipperChildView flipperChildView = (FlipperChildView) viewFlipper.getChildAt(index);
            flipperChildView.onDestroy();
        }
    }
}
