package com.ssl.curriculum.math.task;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.ssl.curriculum.math.logic.PageFlipper;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.service.EdgeContentProvider;

public class FetchEdgeTask extends AsyncTask<Void, Void, ArrayList<Edge>> {
    private EdgeContentProvider provider;
    private PageFlipper pageFlipper;

    public FetchEdgeTask(EdgeContentProvider edgeProvider, PageFlipper pageFlipper) {
        this.provider = edgeProvider;
        this.pageFlipper = pageFlipper;
    }

    @Override
    protected ArrayList<Edge> doInBackground(Void... voids) {
        return provider.fetchMatchedEdges(pageFlipper.curActivityId, pageFlipper.curSectionId);
    }

    @Override
    protected void onPostExecute(ArrayList<Edge> edges) {
        pageFlipper.flip(edges);
    }
}
