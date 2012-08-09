package com.ssl.curriculum.math.model.activity;

import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.sunshine.metadata.provider.MetadataContract.Activities;

import java.util.ArrayList;

public class QuizDomainData extends DomainActivityData {
	private ArrayList<QuizQuestion> questions = new ArrayList<QuizQuestion>();
	
	public QuizDomainData() {
		super(Activities.TYPE_QUIZ);
	}
	
	public void addQuestion(QuizQuestion q){
		this.questions.add(q);
	}

    public int size(){
		return this.questions.size();
	}
	
	public QuizQuestion getQuestionById(int id){
		return this.questions.get(id);
	}

    @Override
    public String getResult() {
        int correctCount = 0;
        for (QuizQuestion question : questions) {
            if (question.isPass()) {
                correctCount++;
            }
        }
        return String.valueOf(correctCount);
    }
}
