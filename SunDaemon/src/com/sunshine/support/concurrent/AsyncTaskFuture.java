package com.sunshine.support.concurrent;

import java.util.Vector;

import android.os.AsyncTask;

public abstract class AsyncTaskFuture<Params, Progress, V> extends AsyncTask<Params,Progress,V> implements ListenableFuture<V>{

	Params[] params;
	Vector<Listener<V>> listeners;
	
	public AsyncTaskFuture(Params... params){
		this.params = params;	
		this.listeners = new Vector<Listener<V>>();
	}		
	
	public void addListener(Listener<V> listener) {
		listeners.add(listener);
	}
	
	@Override
	protected void onPostExecute(V result){
		for(Listener<V> l:listeners){
			l.callback(result);
		}
	}
	
	public void start(){
		execute(params);
	}
}
