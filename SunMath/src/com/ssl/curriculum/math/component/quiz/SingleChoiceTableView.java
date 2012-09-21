package com.ssl.curriculum.math.component.quiz;

import android.content.Context;

public class SingleChoiceTableView extends ChoiceTableView {

    public SingleChoiceTableView(Context context) {
        super(context);
    }

    @Override
    protected void selectChoice(ChoiceButton choiceButton) {
        for (int index = 0; index < tableLayout.getChildCount(); index++) {
            ChoiceTableItemView itemView = (ChoiceTableItemView) tableLayout.getChildAt(index);
            itemView.setSelected(false);
        }
        choiceButton.setSelected(true);
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
