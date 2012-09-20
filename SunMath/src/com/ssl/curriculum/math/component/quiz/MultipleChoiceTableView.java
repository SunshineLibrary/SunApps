package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import com.ssl.curriculum.math.utils.Constants;

import java.util.Arrays;
import java.util.List;

public class MultipleChoiceTableView extends ChoiceTableView {

    public MultipleChoiceTableView(Context context) {
        super(context);
    }

    @Override
    protected void selectChoice(ChoiceButton choiceButton) {
        choiceButton.toggle();
    }

    @Override
    public void checkAnswer(String answer) {
        if (answer == null) return;
        List<String> answers = Arrays.asList(answer.split(Constants.MULTIPLE_CHOICE_ANSWER_SEPARATOR));
        if (answers == null || answers.size() == 0) return;

        for (int index = 0; index < tableLayout.getChildCount(); index++) {
            ChoiceTableItemView itemView = (ChoiceTableItemView) tableLayout.getChildAt(index);
            updateSingleAnswer(answers, itemView);
        }
    }

    private void updateSingleAnswer(List<String> answers, ChoiceTableItemView itemView) {
        if (answers.contains(itemView.getToken())) {
            itemView.showCorrect();
            return;
        }
        if (itemView.isSelected()) {
            itemView.showInCorrect();
        }
    }
}
