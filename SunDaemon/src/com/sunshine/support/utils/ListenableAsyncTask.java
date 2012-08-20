package com.sunshine.support.utils;

import android.os.AsyncTask;

import java.util.List;
import java.util.Vector;

public abstract class ListenableAsyncTask<Param, Progress, Result> extends AsyncTask<Param, Progress, Result> {

    private List<Listener<Result>> listeners;

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        for (Listener listener: getListeners()) {
            listener.onResult(result);
        }
    }

    public void addListener(Listener<Result> listener) {
        getListeners().add(listener);
    }

    private List<Listener<Result>> getListeners() {
        if (listeners == null) {
            listeners = new Vector<Listener<Result>>();
        }
        return listeners;
    }
}
