package com.ssl.curriculum.math.logic;

import java.util.ArrayList;

import android.content.Context;

import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.listener.EdgeReceiver;
import com.ssl.curriculum.math.listener.PageFlipListener;
import com.ssl.curriculum.math.model.ActivityStatus;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.ActivityData;
import com.ssl.curriculum.math.presenter.MainActivityPresenter;
import com.ssl.curriculum.math.task.FetchActivityDataTask;
import com.ssl.curriculum.math.task.FetchEdgeTask;

public class PageFlipper implements EdgeReceiver, ActivityDataReceiver, PageFlipListener {
	private ArrayList<ActivityData> activityStack = new ArrayList<ActivityData>();
	private int currentPosition = 0;
	public ActivityStatus curStatus;
	public MainActivityPresenter presenter;
	
	public PageFlipper(MainActivityPresenter presenter){
		this.presenter = presenter;
		this.presenter.setPageFlipListener(this);
	}
	
	public void init(int id){
		ActivityData pseudoData = new ActivityData(-1);
		pseudoData.setUniqueId(id);
		FetchEdgeTask task = new FetchEdgeTask(presenter.getEdgeProvider(), pseudoData, this);
		task.execute();
		currentPosition = -1;
	}
	
	@Override
	public void onShowNext() {
		if(currentPosition == activityStack.size() - 1){
			FetchEdgeTask task = new FetchEdgeTask(presenter.getEdgeProvider(), this.activityStack.get(this.currentPosition), this);
			task.execute();
		}else{
			currentPosition++;
			presenter.present(this.activityStack.get(currentPosition),1);
		}
	}
	
	@Override
	public void onShowPrevious(){
		if(currentPosition > 0){
			currentPosition --;
			presenter.present(activityStack.get(currentPosition),-1);
		}else{
			/** 
			 * We are at the start of the stack (download unit) and cannot go back further without extra measures
			 * (Such as loading a new section or the such)
			 */
		}
	}
	
	public void getActivityById(int id){
		FetchActivityDataTask task = new FetchActivityDataTask(this, this.presenter.getActvityProvider(),id);
		task.execute();
	}
	
	public void decideNextEdge(ArrayList<Edge> edges){
		for(int i = 0; i < edges.size(); i++){
			if(decideCondition(edges.get(i).getCondition(), curStatus)){
				this.getActivityById(edges.get(i).getToId());
				return;
			}
		}
	}
	
	
	
	public boolean decideCondition(String condition, ActivityStatus curStatus){
		/**
		 * TODO decide which edge to be selected
		 */
		return true;	
	}
	
	@Override
	public void onReceivedActivityData(ActivityData data){
		activityStack.add(data);
		currentPosition++;
		presenter.present(data, 1);
	}
	
	@Override
	public void onReceivedEdges(ArrayList<Edge> edges) {
		this.decideNextEdge(edges);
	}

}
