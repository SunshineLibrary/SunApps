package com.ssl.curriculum.math.model.activity;

public class VideoActivityData {
	private String videoTitle = "";
	private String videoDescription = "";
	private long videoDuration = 0; //Video Duration in .001s
	private int dbid = 0;
	
	public VideoActivityData(){
		
	}
	
	public void initVideoMetadata(String title, String desc, long dur){
		this.videoTitle = title;
		this.videoDescription = desc;
		this.videoDuration = dur;
	}
	
	public void setUniqueId(int id){
		this.dbid = id;
	}
	
	public int getUniqueId(){
		return this.dbid;
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
