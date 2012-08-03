package com.ssl.curriculum.math.model.activity;

import java.util.ArrayList;

import com.sunshine.metadata.provider.MetadataContract.Activities;
import com.ssl.curriculum.math.model.activity.quiz.*;

public class QuizDomainActivityData extends DomainActivityData {
	private ArrayList<QuizQuestion> questions = new ArrayList<QuizQuestion>();
	
	public QuizDomainActivityData() {
		super(Activities.TYPE_QUIZ);
	}
	
	public void addQuestion(QuizQuestion q){
		this.questions.add(q);
	}
	
	public void initQuizMetadata(ArrayList<QuizQuestion> quizQuestionsList){
		this.questions = quizQuestionsList;
	}
	
	public int size(){
		return this.questions.size();
	}
	
	public QuizQuestion getQuestion(int id){
		return this.questions.get(id);
	}
}
