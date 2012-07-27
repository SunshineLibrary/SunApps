package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.service.EdgeContentProvider;

import android.content.Context;
import android.view.View;

public class MainContentPresenter {
	private View mainContentView;
	public EdgeContentProvider edgeProvider;
	
	public MainContentPresenter(Context context){
		edgeProvider = new EdgeContentProvider(context);
	}
	
	public void presentActivityById(int id){
		
	}
}
