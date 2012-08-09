package com.ssl.curriculum.math.service;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.model.activity.SectionActivitiesData;
import com.ssl.curriculum.math.model.activity.SectionActivityData;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.metadata.provider.MetadataContract.Activities;

import java.util.ArrayList;
import java.util.List;

import static com.sunshine.metadata.provider.MetadataContract.SectionComponents;
import static com.sunshine.metadata.provider.MetadataContract.Sections;

public class ActivityContentProvider {

    private Context context;

    public ActivityContentProvider(Context context) {
        this.context = context;
    }

    public void fetchSectionActivityData(ActivityDataReceiver activityDataReceiver, int sectionId, int activityId) {
        SectionActivitiesData sectionActivitiesData = fetchSectionActivities(sectionId);
        DomainActivityData domainActivityData = fetchActivity(activityId);
        List<Edge> currentActivityEdges = fetchEdges(sectionId, activityId);

        activityDataReceiver.onReceivedDomainActivity(domainActivityData);
        activityDataReceiver.onReceivedSectionActivities(sectionActivitiesData);
        activityDataReceiver.onReceivedEdges(currentActivityEdges);
    }

    public SectionActivitiesData fetchSectionActivities(int sectionId) {
        SectionActivitiesData sectionActivitiesData = new SectionActivitiesData(sectionId);
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(Sections.getActivitiesBelongToSection(sectionId), new String[]{
                SectionComponents._ACTIVITY_ID, SectionComponents._SEQUENCE}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int fetchedActivityId = cursor.getInt(getIndex(cursor, SectionComponents._ACTIVITY_ID));
                int sequence = cursor.getInt(getIndex(cursor, SectionComponents._SECTION_ID));
                sectionActivitiesData.addSectionActivity(new SectionActivityData(fetchedActivityId, sequence));
            } while (cursor.moveToNext());
        }

        return sectionActivitiesData;
    }

    private List<Edge> fetchEdges(int sectionId, int activityId) {
        ContentResolver contentResolver = context.getContentResolver();
        ArrayList<Edge> fetchedEdges = new ArrayList<Edge>();
        String[] columns = {MetadataContract.Edges._FROM_ID, MetadataContract.Edges._TO_ID, MetadataContract.Edges._CONDITION};
        Cursor cursor = contentResolver.query(MetadataContract.Edges.CONTENT_URI, columns,
                "SECTION_ID = " + sectionId + " AND " + "_FROM_ID = " + activityId, null, null);
        if (cursor.moveToFirst()) {
            int fromIdIndex = cursor.getColumnIndex(MetadataContract.Edges._FROM_ID);
            int toIdIndex = cursor.getColumnIndex(MetadataContract.Edges._TO_ID);
            int conditionIndex = cursor.getColumnIndex(MetadataContract.Edges._CONDITION);
            do {
                int fromId = cursor.getInt(fromIdIndex);
                int toId = cursor.getInt(toIdIndex);
                String condition = cursor.getString(conditionIndex);
                fetchedEdges.add(new Edge(fromId, toId, condition));
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.i("@FetchedEdges", fetchedEdges.toString());
        return fetchedEdges;
    }

    private DomainActivityData fetchActivity(int activityId) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(Activities.getActivityUri(activityId), new String[]{
                Activities._ID, Activities._NAME, Activities._TYPE,
                Activities._PROVIDER_ID,
                Activities._NOTES, Activities._DURATION},
                Activities._ID + " =" + activityId, null, null);
        if (cursor.moveToFirst()) {
            return createActivityData(activityId, cursor);
        }
        return null;
    }


    private DomainActivityData createActivityData(int activityId, Cursor cursor) {
        DomainActivityData domainActivityData = new DomainActivityData();
        domainActivityData.activityId = activityId;
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
