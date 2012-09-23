package com.ssl.curriculum.math.component.viewer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.component.activity.*;

import static com.ssl.metadata.provider.MetadataContract.Activities.*;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class ActivityViewer extends FrameLayout{

    private ActivityView mCurrentView;
    private FlipAnimationManager mAnimationManager;

    private ActivityView mVideoView, mQuizView, mHtmlView, mTextView, mGalleryView;

    public ActivityViewer(Context context) {
        super(context);
        mAnimationManager = new FlipAnimationManager(context);
    }

    public ActivityViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAnimationManager = new FlipAnimationManager(context);
    }

    public void onNextBtnClicked(View v){
        mCurrentView.onNextBtnClicked(v);
    }

    public void onPrevBtnClicked(View v){
        mCurrentView.onPrevBtnClicked(v);
    }

    public void startActivity(LinkedActivityData activityData) {
        if (mCurrentView != null) {
            mCurrentView.onBeforeFlippingOut();
            mCurrentView.setVisibility(View.INVISIBLE);
        }

        setActivity(activityData);

        mCurrentView.setVisibility(View.VISIBLE);
        mCurrentView.onAfterFlippingIn();
    }

    public void startNextActivity(LinkedActivityData activityData) {
        if (mCurrentView != null) {
            mCurrentView.onBeforeFlippingOut();
            flipCurrentViewOutToLeft();
        }
        setActivity(activityData);

        flipCurrentViewInFromRight();
        mCurrentView.onAfterFlippingIn();
    }

    public void startPreviousActivity(LinkedActivityData activityData) {
        if (mCurrentView != null) {
            mCurrentView.onBeforeFlippingOut();
            flipCurrentViewOutToRight();
        }

        setActivity(activityData);

        flipCurrentViewInFromLeft();
        mCurrentView.onAfterFlippingIn();
    }

    private void setActivity(LinkedActivityData activityData) {
        mCurrentView = getViewForActivity(activityData);
        mCurrentView.setActivity(activityData);
    }

    public void flipCurrentViewInFromLeft() {
        mCurrentView.startAnimation(mAnimationManager.getFlipInFromLeftAnimation());
        mCurrentView.setVisibility(View.VISIBLE);
    }

    public void flipCurrentViewInFromRight() {
        mCurrentView.startAnimation(mAnimationManager.getFlipInFromRightAnimation());
        mCurrentView.setVisibility(View.VISIBLE);
    }

    public void flipCurrentViewOutToLeft() {
        mCurrentView.startAnimation(mAnimationManager.getFlipOutToLeftAnimation());
        mCurrentView.setVisibility(View.INVISIBLE);
    }

    public void flipCurrentViewOutToRight() {
        mCurrentView.startAnimation(mAnimationManager.getFlipOutToRightAnimation());
        mCurrentView.setVisibility(View.INVISIBLE);
    }

    private ActivityView getViewForActivity(LinkedActivityData activityData) {
        switch (activityData.type) {
            case TYPE_VIDEO:
                return getVideoView();
            case TYPE_TEXT:
                return getTextView();
            case TYPE_QUIZ:
                return getQuizView();
            case TYPE_HTML:
                return getHtmlView();
            case TYPE_GALLERY:
                return getGalleryView();
        }
        return null;
    }

    private ActivityView getVideoView() {
        if (mVideoView == null) {
            mVideoView = new VideoActivityView(getContext(), this);
            addView(mVideoView);
        }
        return mVideoView;
    }

    private ActivityView getTextView() {
        if (mTextView == null) {
            mTextView = new TextActivityView(getContext(), this);
            addView(mTextView);
        }
        return mTextView;
    }

    private ActivityView getQuizView() {
        if (mQuizView == null) {
            mQuizView = new QuizActivityView(getContext(), this);
            addView(mQuizView);
        }
        return mQuizView;
    }

    private ActivityView getHtmlView() {
        if (mHtmlView == null) {
            mHtmlView = new HtmlActivityView(getContext(), this);
            addView(mHtmlView);
        }
        return mQuizView;
    }

    private ActivityView getGalleryView() {
        if (mGalleryView == null) {
            mGalleryView = new GalleryActivityView(getContext(), this);
            addView(mGalleryView);
        }
        return mGalleryView;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

    public void destroy() {
        if (mVideoView != null) {
            mVideoView.onDestroy();
        }
        if (mQuizView != null) {
            mQuizView.onDestroy();
        }
    }
}
