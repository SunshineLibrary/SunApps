package com.ssl.curriculum.math.component.quiz;

import android.content.Context;

public class SingleAnswerChoiceTableView extends ChoiceTableView {

    public SingleAnswerChoiceTableView(Context context, QuizQuestionView quizQuestionView) {
        super(context, quizQuestionView);
    }

    @Override
    protected void selectChoice(ChoiceButton choiceButton) {
        for (int index = 0; index < tableLayout.getChildCount(); index++) {
            ChoiceTableItemView itemView = (ChoiceTableItemView) tableLayout.getChildAt(index);
            itemView.setSelected(false);
        }
        choiceButton.setSelected(true);
        showConfirmButton();
    }

    @Override
    public void checkAnswer(String answer) {
        super.checkAnswer(answer);

        for (int index = 0; index < tableLayout.getChildCount(); index++) {
            ChoiceTableItemView itemView = (ChoiceTableItemView) tableLayout.getChildAt(index);
            if (itemView.getToken().equalsIgnoreCase(answer)) {
                itemView.showCorrect();
                if (itemView.isSelected()) {
                    appendUserAnswer(itemView.getToken());
                    return;
                }
            } else if (itemView.isSelected()) {
                itemView.showInCorrect();
                appendUserAnswer(itemView.getToken());
                setIncorrect();
            }
        }
    }
}
