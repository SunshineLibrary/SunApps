package com.ssl.curriculum.math.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.ssl.curriculum.math.model.Edge;
import com.sunshine.metadata.provider.MetadataContract;

import java.util.ArrayList;

public class EdgeContentProvider {
    private Context context;
    private final ContentResolver contentResolver;

    public EdgeContentProvider(Context context) {
        this.context = context;
        contentResolver = this.context.getContentResolver();
    }

    public ArrayList<Edge> fetchMatchedEdges(int activityId, int sectionId) {
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
}
