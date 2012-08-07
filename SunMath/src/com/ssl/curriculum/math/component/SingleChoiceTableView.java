package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;

public class SingleChoiceTableView extends ChoiceTableView {

    public SingleChoiceTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
