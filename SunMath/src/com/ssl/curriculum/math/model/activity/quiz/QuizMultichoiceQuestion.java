package com.ssl.curriculum.math.model.activity.quiz;

import java.util.ArrayList;

public class QuizMultiChoiceQuestion extends QuizQuestion {
	public static int CHOICE_INCORRECT = 0;
	public static int CHOICE_CORRECT = 1;
	
	private class Choice{
		public String source = "";
		public String name = "";
		public int mode = 0;
		public Choice(String source, String name, int mode){
			this.source = source;
			this.mode = mode;
			this.name = name;
		}
	}
	private String source = "";
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	
	public QuizMultiChoiceQuestion() {
		super(QuizQuestion.TYPE_MULTICHOICE);
	}
	
	public void initQuestion(String source){
		this.source = source;
	}
	
	public void addChoice(String choice, String name, int mode){
		choices.add(new Choice(choice, name, mode));
	}
	
	public boolean isCorrect(int selection){
		if(choices.get(selection).mode == CHOICE_CORRECT)
			return true;
		return false;
	}
	
	public String getChoiceHtml(int id){
		return choices.get(id).source;
	}
	
	public String getChoiceName(int id){
		return choices.get(id).name;
	}
	
	public String getQuestionHtml(){
		return this.source;
	}
	
	public int size(){
		return choices.size();
	}
}
