package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import com.ssl.curriculum.math.utils.Constants;

import java.util.Arrays;
import java.util.List;

public class MultipleAnswersChoiceTableView extends ChoiceTableView {

    private int iSelectedCounter;

    public MultipleAnswersChoiceTableView(Context context, QuizQuestionView quizQuestionView) {
        super(context, quizQuestionView);
        iSelectedCounter = 0;
    }

    public void increaseCounter() {
        iSelectedCounter++;
    }

    public void decreaseCounter() {
        iSelectedCounter--;
    }

    @Override
    protected void selectChoice(ChoiceButton choiceButton) {
        choiceButton.toggle(this);
        if (iSelectedCounter > 0) {
            showConfirmButton();
        } else {
            hideConfirmButton();
        }
    }

    @Override
    public void checkAnswer(String answer) {
        super.checkAnswer(answer);

        if (answer == null) return;
        List<String> answers = Arrays.asList(answer.split(Constants.MULTIPLE_CHOICE_ANSWER_SEPARATOR));
        if (answers == null || answers.size() == 0) return;

        for (int index = 0; index < tableLayout.getChildCount(); index++) {
            ChoiceTableItemView itemView = (ChoiceTableItemView) tableLayout.getChildAt(index);
            updateSingleAnswer(answers, itemView);
            if (itemView.isSelected()) {
                appendUserAnswer(itemView.getToken() + Constants.MULTIPLE_CHOICE_ANSWER_SEPARATOR);
            }
        }
    }

    private void updateSingleAnswer(List<String> answers, ChoiceTableItemView itemView) {
        if (answers.contains(itemView.getToken())) {
            itemView.showCorrect();
        } else if (itemView.isSelected()) {
            itemView.showInCorrect();
            setIncorrect();
        }
    }
}
