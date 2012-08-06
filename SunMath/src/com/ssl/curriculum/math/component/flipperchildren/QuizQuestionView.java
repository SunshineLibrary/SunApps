package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.widget.LinearLayout;

public class QuizQuestionView extends LinearLayout {
    private int questionId;

    public QuizQuestionView(Context context, int questionId) {
        super(context);
        this.questionId = questionId;
    }

    public int getQuestionId() {
        return questionId;
    }
}
