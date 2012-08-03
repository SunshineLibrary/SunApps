package com.ssl.curriculum.math.model.activity;

public class DomainActivityData {
	private int id = 0;
	private int type = 0;
    private int sectionId;

    private int activityId;

    public DomainActivityData(int type){
		this.type = type;
	}

    public DomainActivityData(int sectionId, int activityId) {
        this.sectionId = sectionId;
        this.activityId = activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setId(int id){
		this.id = id;
	}

    public int getSectionId(){
		return sectionId;
	}
	
	public int getType(){
		return this.type;
	}
}
