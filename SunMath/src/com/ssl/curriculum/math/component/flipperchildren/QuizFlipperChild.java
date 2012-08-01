package com.ssl.curriculum.math.component.flipperchildren;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.flipperchildren.subviews.QuizFillInSubview;
import com.ssl.curriculum.math.component.flipperchildren.subviews.QuizMultichoiceSubview;
import com.ssl.curriculum.math.model.activity.QuizActivityData;
import com.ssl.curriculum.math.model.activity.quiz.QuizMultichoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizFillBlankQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

@SuppressLint("ViewConstructor")
public class QuizFlipperChild extends LinearLayout{
	private QuizQuestion currentQuestion;
	private int currentPos = 0;
	private QuizActivityData quiz;
	public QuizFlipperChild(Context context, AttributeSet attrs, QuizActivityData quiz) {
		super(context, attrs);
		try{
			View.inflate(context,R.layout.quiz_flip_layout,this);
		}catch(Exception e){
			e.printStackTrace();
		}
		this.quiz = quiz;
		initUI();
		initQuestion();
	}
	
	private void initUI(){
		TextView tv = (TextView) findViewById(R.id.quiz_next_button);
		final QuizFlipperChild self = this;
		tv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				self.nextQuestion();
			}
			
		});
	}
	
	private void initQuestion(){
		if(quiz.size() <= 0)
			return;
		currentQuestion = quiz.getQuestion(0);
		currentPos = 0;
		this.presentQuestion();
	}
	
	private void presentQuestion(){
		if(currentQuestion == null)
			return;
		ViewFlipper questionView = (ViewFlipper) findViewById(R.id.quiz_question_view);
		System.out.println(currentPos);
		switch(currentQuestion.getType()){
			case QuizQuestion.TYPE_MULTICHOICE:{
				QuizMultichoiceSubview multichoice = new QuizMultichoiceSubview(getContext(),null,(QuizMultichoiceQuestion) currentQuestion);
				questionView.addView(multichoice);
			}break;
			case QuizQuestion.TYPE_FILLBLANKS:{
				QuizFillInSubview fillin = new QuizFillInSubview(getContext());
				fillin.loadQuiz((QuizFillBlankQuestion) currentQuestion);
				questionView.addView(fillin);
			}break;
			default:break;
		}
		if(questionView.getChildCount() > 1){
			questionView.showNext();
		}
		while(questionView.getChildCount() > 1){
			questionView.removeViewAt(0);
		}
	}
	
	public void nextQuestion(){
		currentPos++;
		if(currentPos >= quiz.size()){
			currentPos--;
			return;
		}else{
			currentQuestion = quiz.getQuestion(currentPos);
			this.presentQuestion();
		}
	}
	
}