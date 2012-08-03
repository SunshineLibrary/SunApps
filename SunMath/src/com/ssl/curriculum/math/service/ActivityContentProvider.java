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
                Activities._SEQUENCE, Activities._DIFFICULTY, Activities._PROVIDER_ID,
                Activities._NOTES, Activities._LENGTH, Activities._SECTION_ID},
                Activities._ID + " =" + activityId + " and " + Activities._SECTION_ID + " =" + sectionId, null, null);

        if (cursor.moveToFirst()) {
            return createActivityData(activityId, sectionId, cursor);
        }
        return null;
    }

    private DomainActivityData createActivityData(int activityId, int sectionId, Cursor cursor) {
        DomainActivityData domainActivityData = new DomainActivityData(activityId, sectionId);
        domainActivityData.type = cursor.getInt(getIndex(cursor, Activities._TYPE));
        domainActivityData.difficulty = cursor.getInt(getIndex(cursor, Activities._DIFFICULTY));
        domainActivityData.length = cursor.getInt(getIndex(cursor, Activities._LENGTH));
        domainActivityData.name = cursor.getString(getIndex(cursor, Activities._NAME));
        domainActivityData.notes = cursor.getString(getIndex(cursor, Activities._NOTES));
        domainActivityData.providerId = cursor.getInt(getIndex(cursor, Activities._PROVIDER_ID));
        domainActivityData.sequence = cursor.getInt(getIndex(cursor, Activities._SEQUENCE));
        return domainActivityData;
    }

    private int getIndex(Cursor cursor, String name) {
        return cursor.getColumnIndex(name);
    }
}
