package com.ssl.curriculum.math.presenter.activity;

import android.content.Context;

import com.ssl.curriculum.math.model.activity.VideoActivityData;
import com.ssl.curriculum.math.service.VideoActivityContentProvider;
import com.ssl.curriculum.math.task.FetchVideoActivityTask;

public class VideoActivityPresenter {
	private VideoActivityData video;
	private VideoActivityContentProvider provider;
	
	public VideoActivityPresenter(Context context){
		provider = new VideoActivityContentProvider(context);
	}
	
	public void loadVideoData(){
		FetchVideoActivityTask task = new FetchVideoActivityTask(this, provider);
		task.execute();
	}
	
	public int getId(){
		return 0;
	}
	
	public void setVideoData(VideoActivityData video){
		this.video = video;
	}
}
