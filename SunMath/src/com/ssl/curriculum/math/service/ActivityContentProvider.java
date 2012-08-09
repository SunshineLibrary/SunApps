package com.ssl.curriculum.math.service;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.sunshine.metadata.provider.MetadataContract.Activities;

public class ActivityContentProvider {

    private Context context;

    public ActivityContentProvider(Context context) {
        this.context = context;
    }

    public DomainActivityData fetchActivityById(int activityId, int sectionId) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(Activities.CONTENT_URI, new String[]{
                Activities._ID, Activities._NAME, Activities._TYPE,
                Activities._PROVIDER_ID,
                Activities._NOTES, Activities._DURATION},
                Activities._ID + " =" + activityId, null, null);

        if (cursor.moveToFirst()) {
            return createActivityData(activityId, sectionId, cursor);
        }
        return null;
    }

    private DomainActivityData createActivityData(int activityId, int sectionId, Cursor cursor) {
        DomainActivityData domainActivityData = new DomainActivityData(activityId, sectionId);
        domainActivityData.type = cursor.getInt(getIndex(cursor, Activities._TYPE));
        domainActivityData.duration = cursor.getInt(getIndex(cursor, Activities._DURATION));
        domainActivityData.name = cursor.getString(getIndex(cursor, Activities._NAME));
        domainActivityData.notes = cursor.getString(getIndex(cursor, Activities._NOTES));
        domainActivityData.providerId = cursor.getInt(getIndex(cursor, Activities._PROVIDER_ID));
        return domainActivityData;
    }

    private int getIndex(Cursor cursor, String name) {
        return cursor.getColumnIndex(name);
    }
}
