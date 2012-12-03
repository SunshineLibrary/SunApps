package com.ssl.curriculum.math.component.viewer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.component.activity.*;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;

import static com.ssl.metadata.provider.MetadataContract.Activities.*;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class ActivityViewer extends FrameLayout{

    private int mSectionId;
    private ActivityView mCurrentView;
    private FlipAnimationManager mAnimationManager;

    private ActivityView mAudioView, mVideoView, mQuizView, mHtmlView, mTextView, mGalleryView, mPdfView, mFinishView;
    private ImageView mBtnNext;
    private ActivityFinishListener mActivityFinishListener;

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

    public void setNextBtn(ImageView btn) {
        mBtnNext = btn;
        
    }

    public void enableNextBtn() {
        mBtnNext.setEnabled(true);
    }

    public void disableNextBtn() {
        mBtnNext.setEnabled(false);
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
        	case TYPE_AUDIO:
        	return getAudioView();
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
            case TYPE_PDF:
            	return getPdfView();
            case TYPE_FINISH:
                return getFinishView();
        }
        return null;
    }

    private ActivityView getAudioView() {
        if (mAudioView == null) {
        	mAudioView = new AudioActivityView(getContext(), this);
            addView(mAudioView);
System.out.println("getAudioView");
        }
System.out.println("getAudioView");
        return mAudioView;
    }
    
    private ActivityView getVideoView() {
        if (mVideoView == null) {
            mVideoView = new VideoActivityView(getContext(), this);
            addView(mVideoView);
        }
        return mVideoView;
    }
    
    public ActivityView getVideoViewTo(){
    	if(mVideoView != null){
    		return mVideoView;
    	}else{
    		return null;
    	}
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
        return mHtmlView;
    }

    private ActivityView getGalleryView() {
        if (mGalleryView == null) {
            mGalleryView = new GalleryActivityView(getContext(), this);
            addView(mGalleryView);
        }
        return mGalleryView;
    }
    
    private ActivityView getPdfView() {
        if (mPdfView == null) {
        	mPdfView = new PdfActivityView(getContext(), this);
            addView(mPdfView);
        }
        return mPdfView;
    }

    private ActivityView getFinishView() {
        if (mFinishView == null) {
            mFinishView = new FinishActivityView(getContext(), this);
            addView(mFinishView);
        }
        return mFinishView;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

    public void destroy() {
        if (mVideoView != null) {
            mVideoView.onDestroy();
        }
        
        if(mAudioView != null){
        	mAudioView.onDestroy();
        }
        
        if (mQuizView != null) {
            mQuizView.onDestroy();
        }
    }
    
    public void pause(){
    	if (mVideoView != null) {
            mVideoView.onDestroy();
        }
        
        if(mAudioView != null){
        	mAudioView.onDestroy();
        }
    }

    public void setActivityFinishListener(ActivityFinishListener listener) {
        mActivityFinishListener = listener;
    }

    public void finish() {
        if (mActivityFinishListener != null) {
            mActivityFinishListener.onActivityFinish();
        }
    }

    public int getSectionId() {
        return mSectionId;
    }

    public void setSectionId(int mSectionId) {
        this.mSectionId = mSectionId;
    }

    public static interface ActivityFinishListener {
        public void onActivityFinish();
    }
}
