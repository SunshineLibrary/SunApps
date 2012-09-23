package com.ssl.curriculum.math.data;

import android.content.Context;
import android.database.Cursor;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.utils.IterableCursor;
import com.ssl.metadata.provider.MetadataContract;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class EdgeHelper {

    public static Set<Edge> getSectionEdges(Context context, int sectionId) {
        Set<Edge> edges = new HashSet<Edge>();

        Cursor cursor = context.getContentResolver().query(MetadataContract.Edges.CONTENT_URI, null,
                MetadataContract.Edges._SECTION_ID + "=" + sectionId, null, null);

        for (Cursor c : new IterableCursor(cursor)) {
            edges.add(createEdgeFromCursor(c));
        }

        return edges;
    }

    private static Edge createEdgeFromCursor(Cursor cursor) {
        int from = cursor.getInt(cursor.getColumnIndex(MetadataContract.Edges._FROM_ID));
        int to  = cursor.getInt(cursor.getColumnIndex(MetadataContract.Edges._TO_ID));
        String condition = cursor.getString(cursor.getColumnIndex(MetadataContract.Edges._CONDITION));
        return new Edge(from, to, condition);
    }
}
