package com.ssl.curriculum.math.component.flipperchildren.subviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.model.activity.quiz.QuizMultiChoiceQuestion;

public class QuizMultiChoiceSubview extends LinearLayout {

    private QuizMultiChoiceQuestion question;

    public QuizMultiChoiceSubview(Context context, AttributeSet attrs, QuizMultiChoiceQuestion question) {
        super(context, attrs);
        this.question = question;
        initUI();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_multichoice_flip_layout, this, false);
        addView(viewGroup);
    }

}
