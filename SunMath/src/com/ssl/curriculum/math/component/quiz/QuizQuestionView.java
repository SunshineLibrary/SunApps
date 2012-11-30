package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ssl.curriculum.math.component.viewer.QuizComponentViewer;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.presenter.quiz.AudioJavascriptInterface;
import com.ssl.curriculum.math.utils.QuizHtmlLoader;

public abstract class QuizQuestionView extends QuizComponentView {
    protected WebView questionWebView;
    protected ProgressBar progressBar;
    protected TextView  questionTitle;
    

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

        questionWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    onLoadComplete();
                }
            }
        });
        
        //hereLiu
        questionWebView.addJavascriptInterface(new AudioJavascriptInterface(), "audioManager");
    }

    public abstract boolean onQuestionAnswered();

    public abstract void setQuestion(QuizQuestion question, int positionNum);

    protected abstract String getQuizContent();

    protected void resetView() {
        questionWebView.clearView();
        questionWebView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void onLoadComplete() {
        progressBar.setVisibility(View.INVISIBLE);
        questionWebView.setVisibility(View.VISIBLE);
    }

    public void onDestroy() {
        if (questionWebView != null) {
        	//hereLiu
        	questionWebView.loadUrl("javascript:onDestory()");
            questionWebView.destroy();
        }
    }

    protected void loadQuizHtml(String quizContent, int positionNum) {
    	//hereLiu:
        final String data = QuizHtmlLoader.getInstance(getContext()).loadQuestionBodyWithNewContent(quizContent,positionNum);
        
      /*
       * Android thinks file:// schema insecure, so we use http:// here.
       * And for loadDataWithBaseUrl, the first parameter baseUrl has no exact meaning, we just use it
       * to tell Android we use the secure schema: http://
       * 
       *
       * */
        questionWebView.loadDataWithBaseURL("http://test", data, "text/html", "utf-8", null);
    }
}
