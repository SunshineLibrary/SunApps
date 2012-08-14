package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.ChoiceTableView;
import com.ssl.curriculum.math.component.MultipleChoiceTableView;
import com.ssl.curriculum.math.component.SingleChoiceTableView;
import com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;

public class QuizChoiceView extends QuizQuestionView {

    private ChoiceTableView choiceTableView;
    private ViewGroup viewGroup;

    public QuizChoiceView(Context context, int questionId, boolean isSingleChoice) {
        super(context, questionId);
        initUI(isSingleChoice);
        initWebView();
    }

    private void initUI(boolean isSingleChoice) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_choice_flip_layout, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_choice_flipper_child_question);
        confirmButton = (ImageView) findViewById(R.id.quiz_choice_ok_btn);
        nextBtn = (ImageView) findViewById(R.id.quiz_next_btn);
        createChoiceTable(isSingleChoice);
    }

    private void createChoiceTable(boolean isSingleChoice) {
        if (isSingleChoice) {
            choiceTableView = new SingleChoiceTableView(getContext());
        } else {
            choiceTableView = new MultipleChoiceTableView(getContext());
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.quiz_choice_flipper_child_question);
        viewGroup.addView(choiceTableView, layoutParams);
    }

    @Override
    protected void onQuestionFinished() {
        choiceTableView.checkAnswer(presenter.getAnswer(getQuestionId()));
    }

    @Override
    public void loadQuiz(QuizQuestion question) {
        QuizChoiceQuestion quizChoiceQuestion = (QuizChoiceQuestion) question;
        questionWebView.loadDataWithBaseURL("http://test", quizChoiceQuestion.getQuizContent(), "text/html", "utf-8", null);
        choiceTableView.loadChoices(quizChoiceQuestion.getChoices());
    }

}
