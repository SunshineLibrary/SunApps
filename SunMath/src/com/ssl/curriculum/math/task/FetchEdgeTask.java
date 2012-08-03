package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.listener.EdgeReceiver;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.service.EdgeContentProvider;

import java.util.ArrayList;

public class FetchEdgeTask extends AsyncTask<Void, Void, ArrayList<Edge>> {
    private EdgeContentProvider provider;
    private EdgeReceiver edgeReceiver;
    private DomainActivityData domainActivityData;

    public FetchEdgeTask(EdgeContentProvider edgeProvider, EdgeReceiver edgeReceiver, DomainActivityData domainActivityData) {
        this.provider = edgeProvider;
        this.edgeReceiver = edgeReceiver;
        this.domainActivityData = domainActivityData;
    }

    @Override
    protected ArrayList<Edge> doInBackground(Void... voids) {
        return provider.fetchMatchedEdges(domainActivityData.getActivityId(), domainActivityData.getSectionId());
    }

    @Override
    protected void onPostExecute(ArrayList<Edge> edges) {
        edgeReceiver.onReceivedEdges(edges);
    }
}
