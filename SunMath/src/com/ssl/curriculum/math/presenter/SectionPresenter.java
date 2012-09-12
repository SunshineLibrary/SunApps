package com.ssl.curriculum.math.presenter;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
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
import com.sunshine.metadata.provider.MetadataContract;

import java.util.ArrayList;

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
                currentActivities.move(position);
                int activityId = currentActivities.getInt(
                        currentActivities.getColumnIndex(MetadataContract.SectionComponents._ACTIVITY_ID));
                startActivity(activityId);
            }
        });
    }

    public void presentSection(int id) {
        navigationActivity.getContentResolver().unregisterContentObserver(mObserver);
        currentSection = SectionHelper.getSection(navigationActivity, id);
        navigationActivity.setSection(currentSection);
        currentActivities = SectionHelper.getSectionActivitiesCursor(navigationActivity, id);
        navigationActivity.setSectionActivities(currentActivities);
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

    public void startDownload() {
        ContentValues values = new ContentValues();
        values.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_QUEUED);
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

        Uri activityUri;
        if (currentActivities.moveToFirst()) {
            do {
                int activityId = currentActivities.getInt(currentActivities.getColumnIndex(BaseColumns._ID));
                activityUri = MetadataContract.Activities.getActivityUri(activityId);
                navigationActivity.getContentResolver().registerContentObserver(activityUri, false, mObserver);
                ContentProviderOperation operation = ContentProviderOperation.newUpdate(activityUri).withValues(values).build();
                operations.add(operation);
            } while(currentActivities.moveToNext());
            try {
                navigationActivity.getContentResolver().applyBatch(MetadataContract.AUTHORITY, operations);
            } catch (RemoteException e) {
                Log.e(getClass().getName(), "Error Queueing Activity Download", e);
            } catch (OperationApplicationException e) {
                Log.e(getClass().getName(), "Error Queueing Activity Download", e);
            }
        }
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
