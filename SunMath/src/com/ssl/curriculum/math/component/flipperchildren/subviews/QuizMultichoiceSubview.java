package com.ssl.curriculum.math.component.flipperchildren.subviews;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.model.activity.quiz.QuizMultichoiceQuestion;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class QuizMultichoiceSubview extends LinearLayout{
	private QuizMultichoiceQuestion question;
	public QuizMultichoiceSubview(Context context, AttributeSet attrs, QuizMultichoiceQuestion qq) {
		super(context, attrs);
		question = qq;
		initUI();
	}
	
	private void initUI(){
		QuizMultichoiceSubview.inflate(getContext(), R.layout.quiz_multichoice_flip_layout, this);
	}
	
}
