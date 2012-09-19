package com.ssl.resourcecenter.model;

import android.graphics.Bitmap;

public class CategoryGridItem {
	
	private String id;
	private String name;
	private int count;
	private Bitmap image;
	private String description;

	public CategoryGridItem() {
		super();
	}
	
	public CategoryGridItem(String id, String name, int count, Bitmap image, String description) {
		super();
		this.id = id;
		this.name = name;
		this.count = count;
		this.image = image;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

	public String getDescription() {
		return description;
	}
	
	public String getId(){
		return id;
	}
	
	public String toString(){
		return name;
	}
}
