package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.model.activity.quiz.QuizFillBlankQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.presenter.QuizPresenter;
import com.ssl.curriculum.math.service.QuizQuestionsProvider;
import com.ssl.curriculum.math.utils.QuizHtmlLoader;

public class QuizFillInFlipperChild extends LinearLayout {
    private WebView questionWebView;
    private QuizPresenter quizPresenter;

    public QuizFillInFlipperChild(Context context) {
        super(context);
        initUI();
        initComponent();
        initWebView();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_fill_in_flipper_child, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_fill_in_flipper_child_question);
    }

    private void initComponent() {
        quizPresenter = new QuizPresenter(this);
        quizPresenter.loadQuizQuestions(new QuizQuestionsProvider(getContext()), "quiz 1");
    }

    private void initWebView() {
        questionWebView.getSettings().setJavaScriptEnabled(true);
        questionWebView.getSettings().setAllowFileAccess(true);
        questionWebView.getSettings().setDomStorageEnabled(true);
        questionWebView.setScrollBarStyle(0);
    }

    private void loadQuizHtml(String newContent) {
        final String data = QuizHtmlLoader.getInstance(getContext()).loadQuizHtmlWithNewContent(newContent);

        /*
        * Android thinks file:// schema insecure, so we use http:// here.
        * And for loadDataWithBaseUrl, the first parameter baseUrl has no exact meaning, we just use it
        * to tell Android we use the secure schema: http://
        *
        * */
        questionWebView.loadDataWithBaseURL("http://test", data, "text/html", "utf-8", null);
    }

    public void loadQuiz(QuizQuestion quizQuestion) {
        if (quizQuestion.getType() == QuizQuestion.TYPE_FILLBLANKS) {
            loadQuizHtml(((QuizFillBlankQuestion) quizQuestion).getQuizContent());
        }
    }
}
