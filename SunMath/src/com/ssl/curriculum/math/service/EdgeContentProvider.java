package com.ssl.curriculum.math.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.ssl.curriculum.math.model.Edge;
import com.sunshine.metadata.provider.MetadataContract.*;

import java.util.ArrayList;

public class EdgeContentProvider {
    private Context context;
    private final ContentResolver contentResolver;

    public EdgeContentProvider(Context context) {
        this.context = context;
        contentResolver = this.context.getContentResolver();
    }

    public ArrayList<Edge> fetchMatchedEdges(int curActivityId, int curSectionId) {
    	ArrayList<Edge> fetchedEdges = new ArrayList<Edge>();
        String[] columns = {Edges._FROM_ID, Edges._TO_ID, Edges._CONDITION};
        Cursor cursor = contentResolver.query(Edges.CONTENT_URI, columns, 
        		"SECTION_ID = "+curSectionId+" AND "+"_FROM_ID = "+curActivityId, null, null);
        if(cursor.moveToFirst()){
        	int fromIdIndex = cursor.getColumnIndex(Edges._FROM_ID);
        	int toIdIndex = cursor.getColumnIndex(Edges._TO_ID);
        	int conditionIndex = cursor.getColumnIndex(Edges._CONDITION);
        	do{
        		int from_id = cursor.getInt(fromIdIndex);
        		int to_id = cursor.getInt(toIdIndex);
        		String condition = cursor.getString(conditionIndex);
        		fetchedEdges.add(new Edge(from_id, to_id, condition));
        	} while(cursor.moveToNext());
        }
        cursor.close();
        return fetchedEdges;
    }
}
