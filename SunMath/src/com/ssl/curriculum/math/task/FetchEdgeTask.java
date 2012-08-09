package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.listener.EdgeReceiver;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.service.EdgeContentProvider;

import java.util.ArrayList;

public class FetchEdgeTask extends AsyncTask<Void, Void, ArrayList<Edge>> {
    private EdgeContentProvider provider;
    private EdgeReceiver edgeReceiver;
    private int activityId;
    private int sectionId;

    public FetchEdgeTask(EdgeContentProvider edgeContentProvider, EdgeReceiver edgeReceiver, int sectionId, int activityId) {
        this.provider = edgeContentProvider;
        this.edgeReceiver = edgeReceiver;
        this.activityId = activityId;
        this.sectionId = sectionId;
    }

    @Override
    protected ArrayList<Edge> doInBackground(Void... voids) {
        return provider.fetchMatchedEdges(activityId, sectionId);
    }

    @Override
    protected void onPostExecute(ArrayList<Edge> edges) {
        edgeReceiver.onReceivedEdges(edges);
    }
}
