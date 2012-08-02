package com.ssl.curriculum.math.component.flipperchildren.subviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.model.activity.quiz.QuizFillBlankQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.presenter.QuizPresenter;
import com.ssl.curriculum.math.service.QuizQuestionsProvider;
import com.ssl.curriculum.math.utils.QuizHtmlLoader;

public class QuizFillInSubview extends LinearLayout {
    private WebView questionWebView;
    private QuizPresenter quizPresenter;
    private TextView showAnswerField;
    private ImageView confirmButton;

    public QuizFillInSubview(Context context) {
        super(context);
        initUI();
        initWebView();
        initListeners();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_fill_in_flipper_child, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_fill_in_flipper_child_question);
        confirmButton = (ImageView) findViewById(R.id.quiz_fill_in_ok_btn);
        showAnswerField = (TextView) findViewById(R.id.quiz_fill_in_showAnswerField);
    }

    private void initWebView() {
        questionWebView.getSettings().setJavaScriptEnabled(true);
        questionWebView.getSettings().setAllowFileAccess(true);
        questionWebView.getSettings().setDomStorageEnabled(true);
        questionWebView.setScrollBarStyle(0);
    }

    private void initListeners() {
        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //change confirm button
                judgeAndDisplayMessage();
            }
        });
    }

    private void judgeAndDisplayMessage() {
        EditText editText = (EditText) findViewById(R.id.quiz_fill_in_flipper_child_answer);
        String userInput = editText.getText().toString();
        boolean correct = quizPresenter.getCurrentAnswerState(userInput.trim());
        System.out.println(userInput + "d");
        if (correct) {
            showAnswerField.setText("正确！");
        } else {
            showAnswerField.setText(quizPresenter.getCurrentAnswer());
        }
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

    public void loadQuiz(QuizFillBlankQuestion quizQuestion) {
        loadQuizHtml(quizQuestion.getQuizContent());
    }
}
