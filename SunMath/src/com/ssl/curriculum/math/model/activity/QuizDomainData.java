package com.ssl.curriculum.math.model.activity;

import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.metadata.provider.MetadataContract.Activities;

import java.util.ArrayList;
import java.util.List;

public class QuizDomainData extends LinkedActivityData {
	private List<QuizQuestion> questions = new ArrayList<QuizQuestion>();
	
	public QuizDomainData() {
		super(Activities.TYPE_QUIZ);
	}

    public int size(){
		return this.questions.size();
	}
	
	public QuizQuestion getQuestionById(int id) {
        for (QuizQuestion quizQuestion : questions) {
            if (quizQuestion.getId() == id) {
                return quizQuestion;
            }
        }
        return null;
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

    public void setQuestions(List<QuizQuestion> quizQuestions) {
        this.questions = quizQuestions;
    }

    public QuizQuestion getQuestionByPosition(int position) {
        return questions.get(position);
    }
}
