package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;

import static com.ssl.curriculum.math.utils.Constants.EMPTY_ANSWER;

public class MultipleChoiceTableView extends ChoiceTableView {


    public MultipleChoiceTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public String getAnswer() {
        String answer = "";
        for (int index = 0; index < tableLayout.getChildCount(); index++) {
            ChoiceTableItemView itemView = (ChoiceTableItemView) tableLayout.getChildAt(index);
            if (itemView.isSelected()) {
                answer += itemView.getToken() + ",";
            }
        }
        if (!answer.equalsIgnoreCase("")) {
            return answer.substring(0, answer.lastIndexOf(","));
        }
        return EMPTY_ANSWER;
    }

    @Override
    protected void selectChoice(ChoiceButton choiceButton) {
        choiceButton.toggle();
    }
}
