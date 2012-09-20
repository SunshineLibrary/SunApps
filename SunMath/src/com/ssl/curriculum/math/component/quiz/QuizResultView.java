package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import com.ssl.curriculum.math.component.viewer.QuestionViewer;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class QuizResultView extends QuizQuestionView{

    public QuizResultView(Context context, QuestionViewer questionViewer) {
        super(context, questionViewer);
    }

    @Override
    protected void initUI() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onQuestionAnswered() {}
}
