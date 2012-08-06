package com.ssl.curriculum.math.model.activity;

public class DomainActivityData {
    public int type;
    public int sectionId;
    public int activityId;
    public int providerId;
    public int sequence;
    public String name;
    public String notes;
    public int duration;
    public int difficulty;

    public DomainActivityData(int type){
		this.type = type;
	}

    public DomainActivityData(int sectionId, int activityId) {
        this.sectionId = sectionId;
        this.activityId = activityId;
    }

}
