package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.viewer.QuizComponentViewer;
import com.ssl.curriculum.math.listener.QuestionResultListener;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.presenter.quiz.FillBlankQuestionPresenter;

public class FillBlankQuestionView extends QuizQuestionView implements QuestionResultListener {
    private TextView showAnswerField;
    private EditText answerEditText;
    private QuizQuestion mQuestion;

    private FillBlankQuestionPresenter mPresenter;
    private LinearLayout answerContainer;

    public FillBlankQuestionView(Context context, QuizComponentViewer quizComponentViewer) {
        super(context, quizComponentViewer);
        mPresenter = new FillBlankQuestionPresenter(this);
    }

    @Override
    public void setQuestion(QuizQuestion question) {
        mQuestion = question;
        mPresenter.setQuestion(question);
        resetView();
        loadQuizHtml(question.getQuizContent());
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
    public void onQuestionAnswered() {
        mPresenter.onQuestionAnswered();
    }

    @Override
    public void onQuestionResult(QuizQuestion question, String answer, boolean isCorrect) {
        mQuizComponentViewer.onQuestionResult(question, answer, isCorrect);
    }

    @Override
    public void onAfterFlippingIn() {
        super.onAfterFlippingIn();
        mQuizComponentViewer.showConfirmButton();
    }
}
