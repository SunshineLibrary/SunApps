package com.ssl.curriculum.math.model.activity;

public class ActivityData {
	protected int dbid = 0;
	protected int type = 0;
	
	public ActivityData(int type){
		this.type = type;
	}
	
	public void setUniqueId(int id){
		this.dbid = id;
	}
	
	public int getUniqueId(){
		return this.dbid;
	}
	
}
