package com.ssl.curriculum.math.model.activity;

public class DomainActivityData {
    public int type;
    public int activityId;
    public int providerId;
    public String name;
    public String notes;
    public int duration;

    public DomainActivityData(int type){
		this.type = type;
	}

    public DomainActivityData() {

    }

}
