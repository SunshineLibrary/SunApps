package com.ssl.curriculum.math.model.activity;

import java.util.ArrayList;

import com.sunshine.metadata.provider.MetadataContract.Activities;
import com.ssl.curriculum.math.model.activity.quiz.*;

public class QuizActivityData extends ActivityData {
	public static final int TYPE_MULTICHOICE = 0;
	public static final int TYPE_FILL = 1;
	public static final int TYPE_OTHER = 2;
	private int type = -1;
	private ArrayList<QuizQuestion> questions;
	
	public QuizActivityData() {
		super(Activities.TYPE_QUIZ);
	}
	
	public void initQuizMetadata(int type, ArrayList<QuizQuestion> quizQuestionsList){
		this.type = type;
	}
	
	public int getQuizType(){
		return this.type;
	}
}
