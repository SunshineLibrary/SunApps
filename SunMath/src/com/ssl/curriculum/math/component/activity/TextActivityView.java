package com.ssl.curriculum.math.component.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.utils.IOUtils;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;
import com.ssl.metadata.provider.MetadataContract;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: mendlin
 * Date: 12-8-13
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
public class TextActivityView extends ActivityView {

    private TextView tv_notes;
    private TextView tv_main;

    public TextActivityView(Context context, ActivityViewer activityViewer) {
        super(context, activityViewer);
        initUI();
    }

    @Override
    public void setActivity(LinkedActivityData activityData) {
        super.setActivity(activityData);
        tv_notes.setText(activityData.notes);
        new LoadTextTask(activityData.activityId).execute();
    }

    private FileInputStream getTextFileInput(int activityId) {
        ParcelFileDescriptor pfdInput = null;
        try {
            pfdInput = getContext().getContentResolver().openFileDescriptor(MetadataContract.Activities.getActivityTextUri(activityId), "r");
        } catch (FileNotFoundException e) {
            Log.e("TextView", "Activity file not found: " + activityId, e);
        }
        return new ParcelFileDescriptor.AutoCloseInputStream(pfdInput);
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.flipper_text_layout, this, false);
        addView(viewGroup);
        tv_notes = (TextView) findViewById(R.id.flipper_text_notes);
        tv_main = (TextView) findViewById(R.id.flipper_text_main);
    }

    private class LoadTextTask extends AsyncTask {

        int mActivityId;
        String mText;

        public LoadTextTask(int activityId) {
            mActivityId = activityId;
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                FileInputStream fis = getTextFileInput(mActivityId);
                mText = IOUtils.readString(fis);
            } catch(IOException e) {
                Log.e("TextView", "FileInputStream error", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            tv_main.setText(mText);
        }
    }
}
