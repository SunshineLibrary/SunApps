package com.ssl.curriculum.math.component.activity;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.component.FlipperChildView;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;
import com.ssl.metadata.provider.MetadataContract;

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
        updateActivityStatus(mActivityData.activityId);
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

    private void updateActivityStatus(int id) {
        ContentValues values = new ContentValues();
        values.put(MetadataContract.Activities._RESULT, "0");
        values.put(MetadataContract.Activities._STATUS, "done");
        getContext().getContentResolver().update(MetadataContract.Activities.getActivityUri(id), values, null, null);
    }

}
