package com.ssl.curriculum.math.component.activity;

import android.content.Context;
import android.view.View;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.component.FlipperChildView;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public abstract class ActivityView extends FlipperChildView {

    protected ActivityViewer mActivityViewer;
    protected LinkedActivityData mActivityData;

    public ActivityView(Context context, ActivityViewer activityViewer) {
        super(context);
        mActivityViewer = activityViewer;
    }

    public void setActivity(LinkedActivityData activityData) {
        mActivityData = activityData;
    }

    public void onNextBtnClicked(View v) {
        LinkedActivityData next = mActivityData.getNextActivity();
        if (next != null) {
            next.currentPreviousActivityData = mActivityData;
            mActivityViewer.startNextActivity(next);
        }
    }

    public void onPrevBtnClicked(View v) {
        LinkedActivityData prev = mActivityData.getPreviousActivity();
        if (prev != null) {
            mActivityData.currentPreviousActivityData = null;
            mActivityViewer.startPreviousActivity(prev);
        }
    }
}
