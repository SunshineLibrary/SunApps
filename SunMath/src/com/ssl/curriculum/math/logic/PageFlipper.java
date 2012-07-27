package com.ssl.curriculum.math.logic;

import java.util.ArrayList;

import android.content.Context;

import com.ssl.curriculum.math.activity.MainActivity;
import com.ssl.curriculum.math.model.ActivityStatus;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.presenter.MainContentPresenter;
import com.ssl.curriculum.math.service.EdgeContentProvider;
import com.ssl.curriculum.math.task.FetchEdgeTask;

public class PageFlipper {
	public int curActivityId;
	public int curSectionId;
	public ActivityStatus curStatus;
	public MainContentPresenter presenter;
	
	public PageFlipper(MainContentPresenter presenter){
		this.presenter = presenter;
	}
	
	public void showNext(int curActivityId, int curSectionId, ActivityStatus curStatus, Context context) {
		this.curActivityId = curActivityId;
		this.curSectionId = curSectionId;
		FetchEdgeTask task = new FetchEdgeTask(presenter.edgeProvider, this);
		task.execute();
	}
	
	public void flip(ArrayList<Edge> edges){
		for(int i = 0; i < edges.size(); i++){
			if(decideCondition(edges.get(i).getCondition(), curStatus)){
				presenter.presentActivityById(edges.get(i).getToId()); 
			}
		}
	}
	
	public boolean decideCondition(String condition, ActivityStatus curStatus){
		return true;	//TODO complete this method!
	}

}
