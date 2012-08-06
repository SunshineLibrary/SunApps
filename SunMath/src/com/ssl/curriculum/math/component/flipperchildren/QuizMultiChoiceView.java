package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.MultiChoiceJSInterface;

public class QuizMultiChoiceView extends QuizQuestionView {

    public QuizMultiChoiceView(Context context, int questionId) {
        super(context, questionId);
    }

    @Override
    protected void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_multichoice_flip_layout, this, false);
        addView(viewGroup);
        webView = (WebView) findViewById(R.id.quiz_multichoice_flipper_child_question);
    }

    @Override
    protected void initWebView() {
        super.initWebView();
        webView.addJavascriptInterface(new MultiChoiceJSInterface(), "multiChoice");
    }

    @Override
    protected void loadQuizHtml(String quizContent) {
        webView.loadUrl("file:///android_asset/multichoice.html");
    }
}
