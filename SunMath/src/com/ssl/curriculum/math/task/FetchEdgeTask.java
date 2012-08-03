package com.ssl.curriculum.math.task;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.ssl.curriculum.math.listener.EdgeReceiver;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.service.EdgeContentProvider;

public class FetchEdgeTask extends AsyncTask<Void, Void, ArrayList<Edge>> {
    private EdgeContentProvider provider;
    private EdgeReceiver edgeReceiver;
    private DomainActivityData currentDomainActivity;

    public FetchEdgeTask(EdgeContentProvider edgeProvider, DomainActivityData domainActivity, EdgeReceiver edgeReceiver) {
        this.provider = edgeProvider;
        this.edgeReceiver = edgeReceiver;
        this.currentDomainActivity = domainActivity;
    }

    @Override
    protected ArrayList<Edge> doInBackground(Void... voids) {
        return provider.fetchMatchedEdges(currentDomainActivity.getUniqueId(), currentDomainActivity.getSectionId());
    }

    @Override
    protected void onPostExecute(ArrayList<Edge> edges) {
        edgeReceiver.onReceivedEdges(edges);
    }
}
