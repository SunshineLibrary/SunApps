package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.presenter.QuizPresenter;
import com.ssl.curriculum.math.utils.QuizHtmlLoader;

public class QuizFillInView extends QuizQuestionView {
    private WebView questionWebView;
    private TextView showAnswerField;
    private ImageView confirmButton;
    private EditText answerEditText;

    private QuizPresenter presenter;

    public QuizFillInView(Context context, int questionId) {
        super(context, questionId);
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
        answerEditText = (EditText) findViewById(R.id.quiz_fill_in_flipper_child_answer);
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
                judgeAndDisplayMessage();
            }
        });
    }

    private void judgeAndDisplayMessage() {
        String userInput = answerEditText.getText().toString();
        if (presenter.isCorrect(userInput, getQuestionId())) {
            showAnswerField.setText("正确！");
            return;
        }
        showAnswerField.setText(presenter.getAnswer(getQuestionId()));
    }

    private void loadQuizHtml(String quizContent) {
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

    public void setQuizPresenter(QuizPresenter presenter) {
        this.presenter = presenter;
    }
}
