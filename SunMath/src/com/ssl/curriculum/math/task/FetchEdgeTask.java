package com.ssl.curriculum.math.task;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;

import com.ssl.curriculum.math.listener.EdgeReceiver;
import com.ssl.curriculum.math.logic.PageFlipper;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.ActivityData;
import com.ssl.curriculum.math.service.EdgeContentProvider;

public class FetchEdgeTask extends AsyncTask<Void, Void, ArrayList<Edge>> {
    private EdgeContentProvider provider;
    private EdgeReceiver edgeReceiver;
    private ActivityData currentActivity;

    public FetchEdgeTask(EdgeContentProvider edgeProvider, ActivityData activity, EdgeReceiver edgeReceiver) {
        this.provider = edgeProvider;
        this.edgeReceiver = edgeReceiver;
        this.currentActivity = activity;
    }

    @Override
    protected ArrayList<Edge> doInBackground(Void... voids) {
        return provider.fetchMatchedEdges(currentActivity.getUniqueId(), currentActivity.getSectionId());
    }

    @Override
    protected void onPostExecute(ArrayList<Edge> edges) {
        edgeReceiver.onReceivedEdges(edges);
    }
}
