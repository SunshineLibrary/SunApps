package com.sunshine.support.concurrent;

public interface ListenableFuture<V> {
	
	public void addListener(Listener<V> listener);	
	
	public void start();
	
}
