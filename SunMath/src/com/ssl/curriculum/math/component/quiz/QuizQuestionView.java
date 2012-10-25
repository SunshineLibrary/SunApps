package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.webkit.WebView;
import com.ssl.curriculum.math.component.viewer.QuizComponentViewer;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.utils.QuizHtmlLoader;

public abstract class QuizQuestionView extends QuizComponentView {
    protected WebView questionWebView;

    protected QuizQuestion mQuestion;

    public QuizQuestionView(Context context, QuizComponentViewer quizComponentViewer) {
        super(context, quizComponentViewer);
        initUI();
        initWebView();
    }

    protected abstract void initUI();

    protected void initWebView() {
        questionWebView.getSettings().setJavaScriptEnabled(true);
        questionWebView.getSettings().setAllowFileAccess(true);
        questionWebView.getSettings().setDomStorageEnabled(true);
        questionWebView.setScrollBarStyle(0);
    }

    public abstract boolean isQuestionAnswered();
    public abstract void onQuestionAnswered();

    public void setQuestion(QuizQuestion question) {
        mQuestion = question;
        loadQuizHtml(question.getQuizContent());
    }

    public void onDestroy() {
        if (questionWebView != null) {
            questionWebView.destroy();
        }
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
}
