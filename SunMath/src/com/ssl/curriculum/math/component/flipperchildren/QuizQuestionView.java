package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.ssl.curriculum.math.utils.QuizHtmlLoader;

public abstract class QuizQuestionView extends LinearLayout {
    protected WebView webView;

    private int questionId;

    public QuizQuestionView(Context context, int questionId) {
        super(context);
        this.questionId = questionId;
        initUI();
        initWebView();
    }

    protected abstract void initUI();

    public int getQuestionId() {
        return questionId;
    }

    protected void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(0);
    }

    protected void loadQuizHtml(String quizContent) {
        final String data = QuizHtmlLoader.getInstance(getContext()).loadQuizHtmlWithNewContent(quizContent);

        /*
       * Android thinks file:// schema insecure, so we use http:// here.
       * And for loadDataWithBaseUrl, the first parameter baseUrl has no exact meaning, we just use it
       * to tell Android we use the secure schema: http://
       *
       * */
        webView.loadDataWithBaseURL("http://test", data, "text/html", "utf-8", null);
    }

    public void loadQuiz(String quizContent) {
        loadQuizHtml(quizContent);
    }
}
