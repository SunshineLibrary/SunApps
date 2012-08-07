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

public class QuizFillInView extends QuizQuestionView {
    private TextView showAnswerField;
    private ImageView confirmButton;
    private EditText answerEditText;

    private QuizPresenter presenter;

    public QuizFillInView(Context context, int questionId) {
        super(context, questionId);
        initListeners();
    }

    @Override
    protected void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_fill_in_flipper_child, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_fill_in_flipper_child_question);
        confirmButton = (ImageView) findViewById(R.id.quiz_fill_in_ok_btn);
        showAnswerField = (TextView) findViewById(R.id.quiz_fill_in_showAnswerField);
        answerEditText = (EditText) findViewById(R.id.quiz_fill_in_flipper_child_answer);
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

    public void setQuizPresenter(QuizPresenter presenter) {
        this.presenter = presenter;
    }
}
