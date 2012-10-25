package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.viewer.QuizComponentViewer;
import com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;

import static com.ssl.metadata.provider.MetadataContract.Problems.TYPE_MC;
import static com.ssl.metadata.provider.MetadataContract.Problems.TYPE_SC;

public class MultipleChoiceQuestionView extends QuizQuestionView {

    private ChoiceTableView choiceTableView, singleChoiceTableView, multipleChoiceTableView;
    private ViewGroup viewGroup;
    private QuizQuestion mQuestion;


    public MultipleChoiceQuestionView(Context context, QuizComponentViewer quizComponentViewer) {
        super(context, quizComponentViewer);
    }

    @Override
    public void setQuestion(QuizQuestion question) {
        super.setQuestion(question);
        mQuestion = question;
        setAndShowChoiceTableView();
        loadQuiz(question);
    }

    protected void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_choice_flip_layout, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_choice_flipper_child_question);
    }

    public void loadQuiz(QuizQuestion question) {
        QuizChoiceQuestion quizChoiceQuestion = (QuizChoiceQuestion) question;
        choiceTableView.loadChoices(quizChoiceQuestion.getChoices());
    }

    @Override
    public boolean isQuestionAnswered() {
        return !("" == choiceTableView.getUserAnswer());
    }

    @Override
    public void onQuestionAnswered() {
        choiceTableView.checkAnswer(mQuestion.getAnswer());
        mQuizComponentViewer.onQuestionResult(mQuestion, choiceTableView.getUserAnswer(), choiceTableView.isCorrect());
    }

    public void setAndShowChoiceTableView() {
        switch (mQuestion.getType()) {
            case TYPE_SC:
                choiceTableView = getSingleChoiceTableView();
                showSingleChoiceTableView();
                break;
            case TYPE_MC:
                choiceTableView = getMultipleChoiceTableView();
                showMultipleChoiceTableView();
                break;
        }
    }

    private ChoiceTableView getSingleChoiceTableView() {
        if (singleChoiceTableView == null) {
            singleChoiceTableView = new SingleChoiceTableView(getContext());
            viewGroup.addView(singleChoiceTableView, getChoiceTableLayoutParams());
        }
        return singleChoiceTableView;
    }

    public ChoiceTableView getMultipleChoiceTableView() {
        if (multipleChoiceTableView == null) {
            multipleChoiceTableView = new MultipleChoiceTableView(getContext());
            viewGroup.addView(multipleChoiceTableView, getChoiceTableLayoutParams());
        }
        return multipleChoiceTableView;
    }

    private void showSingleChoiceTableView() {
        if (multipleChoiceTableView != null) {
            multipleChoiceTableView.setVisibility(INVISIBLE);
        }
        singleChoiceTableView.setVisibility(VISIBLE);
    }

    private void showMultipleChoiceTableView() {
        if (singleChoiceTableView != null) {
            singleChoiceTableView.setVisibility(INVISIBLE);
        }
        multipleChoiceTableView.setVisibility(VISIBLE);
    }

    private RelativeLayout.LayoutParams getChoiceTableLayoutParams() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.quiz_choice_flipper_child_question);
        return layoutParams;
    }
}
