package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.widget.LinearLayout;
import com.ssl.curriculum.math.component.viewer.QuizComponentViewer;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public abstract class QuizComponentView extends LinearLayout {

    protected QuizComponentViewer mQuizComponentViewer;

    public QuizComponentView(Context context, QuizComponentViewer quizComponentViewer) {
        super(context);
        mQuizComponentViewer = quizComponentViewer;
    }

    public void onAfterFlippingIn() {}

    public void onBeforeFlippingOut() {}

}
