package com.ssl.curriculum.math.presenter.quiz;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.component.quiz.FillBlankQuestionView;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class FillBlankQuestionPresenter {

    private FillBlankQuestionView mQuestionView;
    private QuizQuestion mQuestion;
    private String correctText;

    public FillBlankQuestionPresenter(FillBlankQuestionView fillBlankQuestionView) {
        mQuestionView = fillBlankQuestionView;
        correctText = mQuestionView.getContext().getString(R.string.correct);
    }

    public void setQuestion(QuizQuestion question) {
        mQuestion = question;
    }

    public boolean onQuestionAnswered() {
        String answer = mQuestionView.getUserAnswer();
        boolean isCorrected = isCorrect(answer);
        if (isCorrected) {
            mQuestionView.setShowAnswerText(correctText);
            mQuestionView.onQuestionResult(mQuestion, answer, true);
        } else {
            mQuestionView.setShowAnswerText(mQuestion.getAnswer());
            mQuestionView.onQuestionResult(mQuestion, answer, false);
        }
        return isCorrected;
    }

    public boolean isCorrect(String answer){
        return mQuestion.getAnswer().equals(answer);
    }

    public String getUserAnswer() {
        return mQuestionView.getUserAnswer();
    }

}
