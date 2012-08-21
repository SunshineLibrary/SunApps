package com.sunshine.sunresourcecenter.model;

public class ResourceListGridItem {
	
	private String id;
	private String title;
	private String builder;
	private String tags;
	private int count;
	private int state;
	private String intro;

	public ResourceListGridItem() {
		super();
	}
	
	public ResourceListGridItem(String id, String title, String builder, String tags, int count, String intro) {
		super();
		this.id = id;
		this.title = title;
		this.builder = builder;
		this.tags = tags;
		this.count = count;
		this.intro = intro;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public String getBuilder() {
		return builder;
	}

	public int getCount() {
		return count;
	}

	public int getState() {
		return state;
	}

	public String getIntro() {
		return intro;
	}
	
	public String getId(){
		return id;
	}
	
	public String toString(){
		return id;
	}
}
