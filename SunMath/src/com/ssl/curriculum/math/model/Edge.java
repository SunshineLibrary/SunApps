package com.ssl.curriculum.math.model;

public class Edge {
	private int fromActivityId;
	private int toActivityId;
	private String condition;
	
	public Edge(int fromActivityId, int toActivityId, String condition){
		this.fromActivityId = fromActivityId;
		this.toActivityId = toActivityId;
		this.condition = condition;
	}

    public int getFromActivityId(){
		return fromActivityId;
	}
	
	public int getToActivityId(){
		return toActivityId;
	}

	public String getCondition(){
		return condition;
	}

    @Override
    public String toString() {
        return "@Edge, from_id:" + fromActivityId + ", to_id:" + toActivityId + ", condition:" + condition;
    }
}
