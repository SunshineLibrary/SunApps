package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.OnChoiceChangedListener;
import com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion;

import java.util.List;

public abstract class ChoiceTableView extends LinearLayout implements OnChoiceChangedListener {
    protected TableLayout tableLayout;

    private String userAnswer;
    private boolean isCorrect;
    private QuizQuestionView mQuizQuestionView;

    protected ChoiceTableView(Context context, QuizQuestionView quizQuestionView) {
        super(context);
        mQuizQuestionView = quizQuestionView;
        initUI();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.choice_table_view_layout, this, false);
        this.addView(viewGroup);
        tableLayout = (TableLayout) findViewById(R.id.choice_table_layout);
    }

    public void reset() {
        tableLayout.removeAllViews();
    }

    public void addChoiceTableRow(ChoiceTableItemView itemView) {
        tableLayout.addView(itemView);
        itemView.setOnChoiceChangedListener(this);
    }

    @Override
    public void onChoiceChanged(ChoiceButton choiceButton) {
        selectChoice(choiceButton);
    }

    protected abstract void selectChoice(ChoiceButton choiceButton);

    public void checkAnswer(String answer) {
        //init answer
        userAnswer = "";
        setIncorrect();
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    protected void setIncorrect() {
        isCorrect = false;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    protected void appendUserAnswer(String ans) {
        userAnswer += ans;
    }

    public void loadChoices(List<QuizChoiceQuestion.Choice> choices) {
        reset();
        for (QuizChoiceQuestion.Choice choice : choices) {
            addChoiceTableRow(new ChoiceTableItemView(getContext(), choice.body, choice.choice));
        }
    }

    public void showConfirmButton() {
        this.mQuizQuestionView.mQuizComponentViewer.showConfirmButton();
    }

    public void hideConfirmButton() {
        this.mQuizQuestionView.mQuizComponentViewer.hideConfirmButton();
    }
}
