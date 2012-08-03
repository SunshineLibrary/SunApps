package com.ssl.curriculum.math.model.activity.quiz;

public class QuizQuestion {
	public static final int TYPE_MULTICHOICE = 0;
	public static final int TYPE_FILLBLANKS = 1;
	
	private int type;
	private int id = 0;
	
	public QuizQuestion(int type){
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}
	
	public void setUniqueId(int id){
		this.id = id;
	}
	
	public int getUniqueId(){
		return this.id;
	}
}
