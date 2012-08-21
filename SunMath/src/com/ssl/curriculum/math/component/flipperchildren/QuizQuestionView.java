package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.presenter.QuizPresenter;

public abstract class QuizQuestionView extends LinearLayout {
    protected WebView questionWebView;
    protected ImageView confirmButton;
    protected QuizPresenter presenter;
    protected ImageView nextBtn;

    private int questionId;

    public QuizQuestionView(Context context, int questionId) {
        super(context);
        this.questionId = questionId;
    }

    protected abstract void onQuestionFinished();

    protected void initWebView() {
        questionWebView.getSettings().setJavaScriptEnabled(true);
        questionWebView.getSettings().setAllowFileAccess(true);
        questionWebView.getSettings().setDomStorageEnabled(true);
        questionWebView.setScrollBarStyle(0);
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuizPresenter(QuizPresenter presenter) {
        this.presenter = presenter;
    }

    public abstract void loadQuiz(QuizQuestion question);
}
