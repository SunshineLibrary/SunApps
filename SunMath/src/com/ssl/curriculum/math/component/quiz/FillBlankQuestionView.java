package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.viewer.QuizComponentViewer;
import com.ssl.curriculum.math.listener.QuestionResultListener;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.presenter.quiz.FillBlankQuestionPresenter;

public class FillBlankQuestionView extends QuizQuestionView implements QuestionResultListener {
    private TextView showAnswerField;
    private EditText answerEditText;
    private QuizQuestion mQuestion;
    //private QuizComponentViewer mQuizViewer;

    private FillBlankQuestionPresenter mPresenter;
    private LinearLayout answerContainer;

    public FillBlankQuestionView(Context context, QuizComponentViewer quizComponentViewer) {
        super(context, quizComponentViewer);
        mPresenter = new FillBlankQuestionPresenter(this);
    }

    @Override
    public void setQuestion(QuizQuestion question, int positionNum) {
        mQuestion = question;
        mPresenter.setQuestion(question);
        resetView();
        int quiz_num = question.getQuizNum();
        /*String quizType = "";
        int type = question.getType();填空题暂不需要标明类型的标题*/
        String questionNum = quiz_num+"."+positionNum;
        //hereLiu:
        loadQuizHtml(getQuizContent(), questionNum);
        
        answerEditText.setEnabled(true);
        answerEditText.setTextColor(Color.BLACK);
    }

    @Override
    protected String getQuizContent() {
        return mQuestion.getQuizContent();
    }

    protected void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_fill_in_flipper_child, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_fill_in_flipper_child_question);
        showAnswerField = (TextView) findViewById(R.id.quiz_fill_in_showAnswerField);
        answerEditText = (EditText) findViewById(R.id.quiz_fill_in_flipper_child_answer);
        progressBar = (ProgressBar) findViewById(R.id.quiz_fill_in_progress_bar);
        questionTitle = (TextView) findViewById(R.id.quiz_fill_in_flipper_child_title);
        answerContainer = (LinearLayout) findViewById(R.id.quiz_fill_in_answer_container);
        
        //mQuizViewer = new QuizComponentViewer(getContext());
        
        //forbid ime full screen when the screen orientation is "landscape" 
        answerEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        answerEditText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                            v.getApplicationWindowToken(), 0);
                    }
                    onQuestionAnswered();
                    //mQuizViewer.showNextButton();
                    return true;
                }
				return false;
			}
        });
        
        //hereLiu
        
    }
    
    public void closeKeyBoard() {
    	answerEditText.clearFocus();
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(answerEditText.getApplicationWindowToken(), 0);

    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
System.out.println("HelloWorld");
    	closeKeyBoard();
    	return super.onTouchEvent(event);
    }

    public void setShowAnswerText(String text) {
        showAnswerField.setText(text);
    }

    @Override
    protected void resetView() {
        super.resetView();
        clearTexts();
        answerContainer.setVisibility(View.INVISIBLE);
    }

    public void clearTexts() {
        showAnswerField.setText("");
        answerEditText.setText("");
        answerEditText.clearComposingText();
    }

    public String getUserAnswer() {
        return answerEditText.getText().toString();
    }

    @Override
    protected void onLoadComplete() {
        super.onLoadComplete();
        answerContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQuestionAnswered() {
        return mPresenter.onQuestionAnswered();
    }

    @Override
    public void onQuestionResult(QuizQuestion question, String answer, boolean isCorrect) {
        mQuizComponentViewer.onQuestionResult(question, answer, isCorrect);
        //hereLiu
        mQuizComponentViewer.showNextButton();
        
        answerEditText.setEnabled(false);
        answerEditText.setTextColor(Color.GRAY);
    }

    @Override
    public void onAfterFlippingIn() {
        super.onAfterFlippingIn();
        mQuizComponentViewer.showConfirmButton();
    }
}
