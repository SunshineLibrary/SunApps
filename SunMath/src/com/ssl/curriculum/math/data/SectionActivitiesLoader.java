package com.ssl.curriculum.math.data;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.utils.IterableCursor;
import com.ssl.metadata.provider.MetadataContract;

import java.util.*;

import static com.ssl.metadata.provider.MetadataContract.Activities;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class SectionActivitiesLoader {

    private int mSectionId;
    private Context mContext;
    private ContentResolver mResolver;

    public SectionActivitiesLoader(Context context, int sectionId) {
        mContext = context;
        mSectionId = sectionId;
        mResolver = context.getContentResolver();
    }

    public LinkedActivityData getActivity(int id) {
        Cursor cursor = mResolver.query(MetadataContract.Activities.getActivityUri(id), null, null, null, null);

        if (cursor.moveToFirst()) {
            LinkedActivityData data = createFromCursor(cursor);
            cursor.close();
            return data;
        } else {
            return null;
        }
    }

    public LinkedActivityData getLoadedActivity(int activityId) {
        return getDecoratedSectionActivitiesMap().get(activityId);
    }


    private Map<Integer, LinkedActivityData> getDecoratedSectionActivitiesMap() {
        Set<Edge> edges = EdgeHelper.getSectionEdges(mContext, mSectionId);
        Map<Integer, LinkedActivityData> activitiesMap = getUndecoratedSectionActivitiesMap();

        LinkedActivityData from, to;
        for (Edge edge : edges) {
            from = activitiesMap.get(edge.getFromActivityId());
            to = activitiesMap.get(edge.getToActivityId());
            if (from != null && to != null) {
                from.putConditionalNextActivity(edge.getCondition(), to);
            }
        }

        return activitiesMap;
    }

    private Map<Integer, LinkedActivityData> getUndecoratedSectionActivitiesMap() {
        Map<Integer, LinkedActivityData> activityDataMap = new HashMap<Integer, LinkedActivityData>();

        String orderBy = MetadataContract.SectionComponents._SEQUENCE;
        Cursor cursor = mResolver.query(MetadataContract.Sections.getSectionActivitiesUri(mSectionId)
                , null, null, null, orderBy);

        LinkedActivityData previous, current;
        previous = current = null;
        for (Cursor c: new IterableCursor(cursor)) {
            previous = current;
            current = createFromCursor(c);

            activityDataMap.put(current.activityId, current);

            if (previous != null) {
                current.defaultPreviousActivityData = previous;
                previous.defaultNextActivityData = current;
            }
        }

        // add finish activity as the last one
        LinkedActivityData finishActivityData = new LinkedActivityData(Activities.TYPE_FINISH);
        //    hacking here, on Server, id is stored as -1 if pointed to the end of a section
        current.defaultNextActivityData = finishActivityData;
        activityDataMap.put(-1, finishActivityData);

        return activityDataMap;
    }

    private static LinkedActivityData createFromCursor(Cursor cursor) {
        int type = cursor.getInt(cursor.getColumnIndex(Activities._TYPE));
        LinkedActivityData data = new LinkedActivityData(type);

        data.activityId = cursor.getInt(cursor.getColumnIndex(Activities._ID));
        data.name = cursor.getString(cursor.getColumnIndex(Activities._NAME));
        data.notes = cursor.getString(cursor.getColumnIndex(Activities._NOTES));

        return data;
    }
}
