package com.ssl.curriculum.math.service.mock;

import android.content.Context;
import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.service.ActivityContentProvider;

import java.util.ArrayList;

public class MockActivityContentProvider extends ActivityContentProvider {

    public MockActivityContentProvider(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Edge> fetchMatchedActivityData(ActivityDataReceiver activityDataReceiver, int activityId, int sectionId) {
        ArrayList<Edge> fetchedEdges = new ArrayList<Edge>();
        fetchedEdges.add(new Edge(1, 2, "Cond"));
        return fetchedEdges;
    }
}
