package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.ssl.curriculum.math.R;

public class QuizMultiChoiceView extends QuizQuestionView {

    public QuizMultiChoiceView(Context context, int questionId) {
        super(context, questionId);
        initUI();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_multichoice_flip_layout, this, false);
        addView(viewGroup);
    }

}
