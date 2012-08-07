package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.ssl.curriculum.math.presenter.QuizPresenter;
import com.ssl.curriculum.math.utils.QuizHtmlLoader;

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

    protected void loadQuizHtml(String quizContent) {
        final String data = QuizHtmlLoader.getInstance(getContext()).loadQuizHtmlWithNewContent(quizContent);

        /*
       * Android thinks file:// schema insecure, so we use http:// here.
       * And for loadDataWithBaseUrl, the first parameter baseUrl has no exact meaning, we just use it
       * to tell Android we use the secure schema: http://
       *
       * */
        questionWebView.loadDataWithBaseURL("http://test", data, "text/html", "utf-8", null);
    }

    public void loadQuiz(String quizContent) {
        loadQuizHtml(quizContent);
    }
}
