package com.ssl.curriculum.math.model;

public class Edge {
	private int from_id;
	private int to_id;
	private String condition;
	
	public Edge(int fid, int tid, String cdn){
		from_id = fid;
		to_id = tid;
		condition = cdn;
	}
	
	public void setFromId(int fid){
		from_id = fid;
	}
	
	public void setToId(int tid){
		to_id = tid;
	}
	
	public void setCondition(String cdn){
		condition = cdn;
	}
	
	public int getFromId(){
		return from_id;
	}
	
	public int getToId(){
		return to_id;
	}

	public String getCondition(){
		return condition;
	}
}
