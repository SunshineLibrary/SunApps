package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;

import static com.ssl.curriculum.math.utils.Constants.EMPTY_ANSWER;

public class SingleChoiceTableView extends ChoiceTableView {

    public SingleChoiceTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public String getAnswer() {
        for (int index = 0; index < tableLayout.getChildCount(); index++) {
            ChoiceTableItemView itemView = (ChoiceTableItemView) tableLayout.getChildAt(index);
            if (itemView.isSelected()) {
                return itemView.getToken();
            }
        }
        return EMPTY_ANSWER;
    }

    @Override
    protected void selectChoice(ChoiceButton choiceButton) {
        System.out.println("-----------------choiceButton = " + choiceButton);
        for (int index = 0; index < tableLayout.getChildCount(); index++) {
            ChoiceTableItemView itemView = (ChoiceTableItemView) tableLayout.getChildAt(index);
            itemView.setSelected(false);
        }
        choiceButton.setSelected(true);
    }
}
