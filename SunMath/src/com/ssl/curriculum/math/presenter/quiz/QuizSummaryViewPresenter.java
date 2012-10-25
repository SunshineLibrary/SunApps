package com.ssl.curriculum.math.presenter.quiz;

import android.content.Context;
import com.ssl.curriculum.math.component.quiz.QuizSummaryView;
import com.ssl.curriculum.math.listener.QuestionResultListener;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class QuizSummaryViewPresenter implements QuestionResultListener{

    private Context mContext;
    private QuizSummaryView mSummaryView;

    private int numCorrect;
    private int numIncorrect;

    public QuizSummaryViewPresenter(Context context, QuizSummaryView summaryView) {
        mContext = context;
        mSummaryView = summaryView;
    }

    @Override
    public void onQuestionResult(QuizQuestion question, String answer, boolean isCorrect) {
        if (isCorrect) {
            numCorrect ++;
        } else {
            numIncorrect ++;
        }
    }

    public String getScore() {
        return numCorrect + "/" + (numIncorrect + numCorrect);
    }


    public void onDisplayed() {
        mSummaryView.setCorrectCount(String.valueOf(numCorrect));
        mSummaryView.setScore(getScore());
        if (numIncorrect == 0) {
            mSummaryView.setGreat();
        } else if (numCorrect > numIncorrect) {
            mSummaryView.setGood();
        } else {
            mSummaryView.setTryAgain();
        }
    }
}
