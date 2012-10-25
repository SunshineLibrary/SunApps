package com.ssl.curriculum.math.model.activity;

public class DomainActivityData {
    private static final String EMPTY_RESULT = "";

    public int type;
    public int activityId;
    public int providerId;
    public String name;
    public String notes;
    public int duration;
    public String result;

    public DomainActivityData(int type) {
        this.type = type;
        this.result = EMPTY_RESULT;
    }

    public DomainActivityData() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
