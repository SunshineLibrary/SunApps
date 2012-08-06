package com.ssl.curriculum.math.model.activity;

import com.sunshine.metadata.provider.MetadataContract.Activities;

public class VideoDomainActivityData extends DomainActivityData {
	private String videoTitle = "";
	private String videoDescription = "";
	private long videoDuration = 0;


    public VideoDomainActivityData(int sectionId, int activityId) {
        super(sectionId, activityId);
    }

    public VideoDomainActivityData(){
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
		int hours = (int)Math.floor(this.videoDuration / 3600);
		int minutes = (int)Math.floor(this.videoDuration / 60) - hours * 60;
		int seconds = (int)(this.videoDuration % 60);
		String durationText = "";
		
		if(hours > 0)
			durationText = hours + ":";
		if(minutes > 9 || hours == 0)
			durationText += minutes + ":";
		else
			durationText += "0" + minutes + ":";
		
		durationText += seconds > 9 ? seconds : "0" + seconds; 
		return durationText;
	}
}
