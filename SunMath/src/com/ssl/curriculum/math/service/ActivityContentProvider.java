package com.ssl.curriculum.math.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.model.activity.QuizDomainData;
import com.sunshine.metadata.provider.MetadataContract;

import java.util.ArrayList;
import java.util.List;

public class ActivityContentProvider {
    private Context context;

    public ActivityContentProvider(Context context) {
        this.context = context;
    }

    public void fetchMatchedActivityData(ActivityDataReceiver activityDataReceiver, int activityId, int sectionId) {
        DomainActivityData domainActivityData = fetchActivity(activityId);
        List<Edge> currentActivityEdges = fetchEdges(sectionId, activityId);

        activityDataReceiver.onReceivedDomainActivity(domainActivityData);
        activityDataReceiver.onReceivedEdges(currentActivityEdges);
    }

    private List<Edge> fetchEdges(int sectionId, int activityId) {
        ContentResolver contentResolver = context.getContentResolver();
        ArrayList<Edge> fetchedEdges = new ArrayList<Edge>();
        String[] columns = {MetadataContract.Edges._FROM_ID, MetadataContract.Edges._TO_ID, MetadataContract.Edges._CONDITION};
        Cursor cursor = contentResolver.query(MetadataContract.Edges.CONTENT_URI, columns,
                MetadataContract.Edges._SECTION_ID + " = ?" + " AND " +  MetadataContract.Edges._FROM_ID + " = ?", new String[]{String.valueOf(sectionId), String.valueOf(activityId)}, null);
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
        Cursor cursor = contentResolver.query(MetadataContract.Activities.getActivityUri(activityId), new String[]{
                MetadataContract.Activities._ID, MetadataContract.Activities._NAME, MetadataContract.Activities._TYPE,
                MetadataContract.Activities._PROVIDER_ID,
                MetadataContract.Activities._NOTES, MetadataContract.Activities._DURATION},
                MetadataContract.Activities._ID + " =" + activityId, null, null);
        if (cursor.moveToFirst()) {
            return createActivityData(activityId, cursor);
        }
        return null;
    }

    private DomainActivityData createActivityData(int activityId, Cursor cursor) {
        int type = cursor.getInt(getIndex(cursor, MetadataContract.Activities._TYPE));
        DomainActivityData domainActivityData = new DomainActivityData();
        if (type == MetadataContract.Activities.TYPE_QUIZ) {
            domainActivityData = new QuizDomainData();
        }
        domainActivityData.activityId = activityId;
        domainActivityData.type = type;
        domainActivityData.duration = cursor.getInt(getIndex(cursor, MetadataContract.Activities._DURATION));
        domainActivityData.name = cursor.getString(getIndex(cursor, MetadataContract.Activities._NAME));
        domainActivityData.notes = cursor.getString(getIndex(cursor, MetadataContract.Activities._NOTES));
        domainActivityData.providerId = cursor.getInt(getIndex(cursor, MetadataContract.Activities._PROVIDER_ID));
        return domainActivityData;
    }

    private int getIndex(Cursor cursor, String name) {
        return cursor.getColumnIndex(name);
    }
}
