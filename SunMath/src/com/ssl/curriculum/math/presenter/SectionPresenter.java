package com.ssl.curriculum.math.presenter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
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
                Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
                int status = cursor.getInt(cursor.getColumnIndex(MetadataContract.Activities._DOWNLOAD_STATUS));
                switch (status) {
                    case MetadataContract.Downloadable.STATUS_DOWNLOADED:
                        startActivity((int) id);
                        break;
                }
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
        registerObserver();
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

    public void registerObserver() {
        ContentResolver resolver = navigationActivity.getContentResolver();
        resolver.unregisterContentObserver(mObserver);
        resolver.registerContentObserver(MetadataContract.Sections.getSectionUri(currentSection.id), false, mObserver);
    }


    public void startDownload() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                ContentValues values = new ContentValues();
                values.put(MetadataContract.Downloadable._DOWNLOAD_STATUS,
                        MetadataContract.Downloadable.STATUS_QUEUED);
                ContentResolver resolver = navigationActivity.getContentResolver();
                resolver.update(MetadataContract.Sections.getSectionUri(currentSection.id), values, null, null);
                return null;
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
            currentSection = SectionHelper.getSection(navigationActivity, currentSection.id);
            showDownloadButton();
            navigationActivity.notifySectionContentChange();
        }
    }
}
