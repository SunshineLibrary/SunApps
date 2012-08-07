package com.ssl.curriculum.math.component;

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
        for (int index = 0; index < tableLayout.getChildCount(); index++) {
            ChoiceTableItemView itemView = (ChoiceTableItemView) tableLayout.getChildAt(index);
            if (itemView.isSelected()) {
                updateSelectedItem(itemView, answer);
            }
        }
    }

    private void updateSelectedItem(ChoiceTableItemView itemView, String answer) {
        if (itemView.getToken().equalsIgnoreCase(answer)) {
            itemView.showCorrect();
            return;
        }
        itemView.showInCorrect();
    }
}
