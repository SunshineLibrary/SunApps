package com.sunshine.sunresourcecenter;

public class CategoryGridItem {
	
	private String id;
	private String name;
	private int count;
	private int imageId;
	private String description;

	public CategoryGridItem() {
		super();
	}
	
	public CategoryGridItem(String id, String name, int count, int imageId, String description) {
		super();
		this.id = id;
		this.name = name;
		this.count = count;
		this.imageId = imageId;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

	public int getImageId() {
		return imageId;
	}

	public String getDescription() {
		return description;
	}
	
	public String getId(){
		return id;
	}
	
	public String toString(){
		return id;
	}
}
