package com.ssl.curriculum.math.model.activity;

import com.sunshine.metadata.provider.MetadataContract.Activities;

public class VideoActivityData extends ActivityData{
	private String videoTitle = "";
	private String videoDescription = "";
	private long videoDuration = 0; //Video Duration in .001s
	
	public VideoActivityData(){
		super(Activities.TYPE_VIDEO);
	}
	
	public void initVideoMetadata(String title, String desc, long dur){
		this.videoTitle = title;
		this.videoDescription = desc;
		this.videoDuration = dur;
	}
	
	public String getTitle(){
		return this.videoTitle;
	}
	
	public String getDescription(){
		return this.videoDescription;
	}
	
	public long getDuration(){
		return this.videoDuration;
	}
	
	public String getDurationText(){
		return "";
	}
}
