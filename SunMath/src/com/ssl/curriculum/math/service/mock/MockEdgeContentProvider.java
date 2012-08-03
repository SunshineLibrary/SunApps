package com.ssl.curriculum.math.service.mock;

import android.content.Context;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.service.EdgeContentProvider;

import java.util.ArrayList;

public class MockEdgeContentProvider extends EdgeContentProvider {

    public MockEdgeContentProvider(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Edge> fetchMatchedEdges(int activityId, int sectionId) {
        ArrayList<Edge> fetchedEdges = new ArrayList<Edge>();
        fetchedEdges.add(new Edge(1, 2, "Cond"));
        fetchedEdges.add(new Edge(1, 2, "Cond"));
        fetchedEdges.add(new Edge(2, 3, "Cond"));
        fetchedEdges.add(new Edge(1, 3, "Cond"));
        return fetchedEdges;
    }
}
