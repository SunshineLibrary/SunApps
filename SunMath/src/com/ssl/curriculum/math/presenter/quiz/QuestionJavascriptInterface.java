package com.ssl.curriculum.math.presenter.quiz;

import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class QuestionJavascriptInterface {

    private MultipleChoiceQuestionPresenter mPresenter;
    private boolean correct;
    private String userAnswer;

    public QuestionJavascriptInterface(MultipleChoiceQuestionPresenter presenter) {
        mPresenter = presenter;
    }

    public void setAnswer(String answer) {
        mPresenter.setAnswer(answer);
    }

    public void unsetAnswer(String answer) {
        mPresenter.unsetAnswer(answer);
    }

    public boolean isCorrectAnswer(String answer) {
        return mPresenter.isCorrectAnswer(answer);
    }
}
