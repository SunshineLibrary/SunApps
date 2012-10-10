package com.ssl.curriculum.math.presenter;

import android.content.*;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.ssl.curriculum.math.activity.MainActivity;
import com.ssl.curriculum.math.activity.NavigationActivity;
import com.ssl.curriculum.math.data.SectionHelper;
import com.ssl.curriculum.math.model.Section;
import com.ssl.metadata.provider.MetadataContract;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class SectionPresenter {

    private NavigationActivity navigationActivity;

    private Section currentSection;
    private Cursor currentActivities;
    private MContentObserver mObserver;

    public SectionPresenter(NavigationActivity navigationActivity) {
        this.navigationActivity = navigationActivity;
        mObserver = new MContentObserver(new Handler());

        navigationActivity.setOnActivityClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity((int) id);
            }
        });
    }

    public void presentSection(int id) {
        navigationActivity.getContentResolver().unregisterContentObserver(mObserver);
        currentSection = SectionHelper.getSection(navigationActivity, id);
        navigationActivity.setSection(currentSection);
        currentActivities = SectionHelper.getSectionActivitiesCursor(navigationActivity, id);
        navigationActivity.setSectionActivities(currentActivities);
        showDownloadButton();
        registerObservers();
    }

    private void showDownloadButton() {
        if (currentSection.download_status == MetadataContract.Downloadable.STATUS_NOT_DOWNLOADED) {
            navigationActivity.showDownloadLesson();
        } else if (currentSection.download_status == MetadataContract.Downloadable.STATUS_DOWNLOADED) {
            navigationActivity.showDownloaded();
        } else {
            navigationActivity.showDownloading();
        }
    }

    private void loadActivities() {
        CursorLoader loader = SectionHelper.getSectionActivitiesCursorLoader(navigationActivity, currentSection.id);
        loader.registerListener(0, new Loader.OnLoadCompleteListener<Cursor>() {
          @Override
          public void onLoadComplete(Loader<Cursor> cursorLoader, Cursor cursor) {
              currentActivities = cursor;
              navigationActivity.setSectionActivities(currentActivities);
            }
        });
        loader.startLoading();
    }

    public void registerObservers() {
        ContentResolver resolver = navigationActivity.getContentResolver();
        resolver.unregisterContentObserver(mObserver);

        Uri activityUri;
        int activityId;
        try {
            if (currentActivities.moveToFirst()) {
                do {
                    activityId = currentActivities.getInt(currentActivities.getColumnIndex(BaseColumns._ID));
                    activityUri = MetadataContract.Activities.getActivityUri(activityId);
                    resolver.registerContentObserver(activityUri, false, mObserver);
                } while (currentActivities.moveToNext());
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), "Failed registering observer", e);
        }
    }


    public void startDownload() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                ContentValues values = new ContentValues();
                values.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_QUEUED);
                ContentResolver resolver = navigationActivity.getContentResolver();

                Uri activityUri;
                try {
                    if (currentActivities.moveToFirst()) {
                        do {
                        int status = currentActivities.getInt(
                                currentActivities.getColumnIndex(MetadataContract.Downloadable._DOWNLOAD_STATUS));
                        if (status == MetadataContract.Downloadable.STATUS_NOT_DOWNLOADED) {
                            int activityId = currentActivities.getInt(currentActivities.getColumnIndex(BaseColumns._ID));
                            activityUri = MetadataContract.Activities.getActivityUri(activityId);
                            resolver.update(activityUri, values, null, null);
                        }
                        } while (currentActivities.moveToNext());
                    }
                } catch (Exception e) {
                    Log.e(getClass().getName(), "Error starting download:", e);
                } finally {
                    return null;
                }
            }
        }.execute();
    }

    public void startActivity(int activityId) {
        Intent intent = new Intent();
        intent.putExtra("sectionId", currentSection.id);
        intent.putExtra("activityId", activityId);
        intent.setClass(navigationActivity, MainActivity.class);
        navigationActivity.startActivity(intent);
    }

    private class MContentObserver extends ContentObserver {

        public MContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            loadActivities();
        }
    }
}
