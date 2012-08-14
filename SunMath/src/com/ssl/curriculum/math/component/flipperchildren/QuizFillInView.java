package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.utils.QuizHtmlLoader;

public class QuizFillInView extends QuizQuestionView {
    private TextView showAnswerField;
    private EditText answerEditText;

    public QuizFillInView(Context context, int questionId) {
        super(context, questionId);
        initUI();
        initWebView();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_fill_in_flipper_child, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_fill_in_flipper_child_question);
        confirmButton = (ImageView) findViewById(R.id.quiz_fill_in_ok_btn);
        nextBtn = (ImageView) findViewById(R.id.quiz_next_btn);
        showAnswerField = (TextView) findViewById(R.id.quiz_fill_in_showAnswerField);
        answerEditText = (EditText) findViewById(R.id.quiz_fill_in_flipper_child_answer);
    }

    @Override
    protected void onQuestionFinished() {
        String userInput = answerEditText.getText().toString();
        if (presenter.isCorrect(userInput, getQuestionId())) {
            showAnswerField.setText(getResources().getText(R.string.correct));
            return;
        }
        showAnswerField.setText(presenter.getAnswer(getQuestionId()));
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

    @Override
    public void loadQuiz(QuizQuestion question) {
        loadQuizHtml(question.getQuizContent());
    }
}
