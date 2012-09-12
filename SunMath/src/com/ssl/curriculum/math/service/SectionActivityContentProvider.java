package com.ssl.curriculum.math.service;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.ssl.curriculum.math.listener.SectionActivityDataReceiver;
import com.ssl.curriculum.math.model.activity.SectionActivitiesData;
import com.ssl.curriculum.math.model.activity.SectionActivityData;

import static com.sunshine.metadata.provider.MetadataContract.SectionComponents;
import static com.sunshine.metadata.provider.MetadataContract.Sections;

public class SectionActivityContentProvider {

    private Context context;
    private ActivityContentProvider activityContentProvider;

    public SectionActivityContentProvider(Context context) {
        this.context = context;
        activityContentProvider = new ActivityContentProvider(context);
    }

    public void fetchSectionActivityData(SectionActivityDataReceiver sectionActivityDataReceiver, int sectionId, int activityId) {
        SectionActivitiesData sectionActivitiesData = fetchSectionActivities(sectionId);
        sectionActivityDataReceiver.onReceivedSectionActivities(sectionActivitiesData);
        activityContentProvider.fetchMatchedActivityData(sectionActivityDataReceiver, activityId, sectionId);
    }

    public SectionActivitiesData fetchSectionActivities(int sectionId) {
        SectionActivitiesData sectionActivitiesData = new SectionActivitiesData();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(Sections.getSectionActivitiesUri(sectionId), new String[]{
                SectionComponents._ACTIVITY_ID, SectionComponents._SEQUENCE}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int fetchedActivityId = cursor.getInt(getIndex(cursor, SectionComponents._ACTIVITY_ID));
                int sequence = cursor.getInt(getIndex(cursor, SectionComponents._SEQUENCE));
                sectionActivitiesData.addSectionActivity(new SectionActivityData(fetchedActivityId, sequence));
            } while (cursor.moveToNext());
        }

        cursor.close();

        Log.i("@fetch section activities", sectionActivitiesData.toString());
        
        return sectionActivitiesData;
    }

    private int getIndex(Cursor cursor, String name) {
        return cursor.getColumnIndex(name);
    }
}
