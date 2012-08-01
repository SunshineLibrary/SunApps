package com.ssl.curriculum.math.component.flipperchildren;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.flipperchildren.subviews.QuizFillInSubview;
import com.ssl.curriculum.math.component.flipperchildren.subviews.QuizMultichoiceSubview;
import com.ssl.curriculum.math.listener.QuizLoadedListener;
import com.ssl.curriculum.math.model.activity.QuizActivityData;
import com.ssl.curriculum.math.model.activity.quiz.QuizMultichoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizFillBlankQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.presenter.QuizPresenter;
import com.ssl.curriculum.math.service.QuizQuestionsProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

@SuppressLint("ViewConstructor")
public class QuizFlipperChild extends LinearLayout implements QuizLoadedListener{
	private QuizPresenter presenter;
	private QuizActivityData quiz;
	
	public QuizFlipperChild(Context context, AttributeSet attrs, QuizActivityData quiz) {
		super(context, attrs);
		try{
			View.inflate(context,R.layout.quiz_flip_layout,this);
		}catch(Exception e){
			e.printStackTrace();
		}
		this.quiz = quiz;
		this.presenter = new QuizPresenter(quiz, new QuizQuestionsProvider(getContext()));

		initUI();
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
		if(this.presenter.toFirst())
			this.present(presenter.getQuestion());
	}
	
	private void present(QuizQuestion question){
		ViewFlipper questionView = (ViewFlipper) findViewById(R.id.quiz_question_view);
		switch(question.getType()){
			case QuizQuestion.TYPE_MULTICHOICE:{
				QuizMultichoiceSubview multichoice = new QuizMultichoiceSubview(getContext(),null,(QuizMultichoiceQuestion) question);
				questionView.addView(multichoice);
			}break;
			case QuizQuestion.TYPE_FILLBLANKS:{
				QuizFillInSubview fillin = new QuizFillInSubview(getContext());
				fillin.loadQuiz((QuizFillBlankQuestion) question);
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
		if(this.presenter.toNext()){
			this.present(this.presenter.getQuestion());
		}
	}

	@Override
	public void onQuizLoaded() {
		this.initQuestion();
	}
	
}