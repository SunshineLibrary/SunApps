package com.ssl.curriculum.math.presenter.quiz;

import android.util.Log;
import com.ssl.curriculum.math.component.quiz.MultipleChoiceQuestionView;
import com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.metadata.provider.MetadataContract;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class MultipleChoiceQuestionPresenter {

    private QuestionJavascriptInterface mJSInterface;
    private MultipleChoiceQuestionView mQuestionView;
    private QuizChoiceQuestion mQuestion;
    private String userAnswer;

    public MultipleChoiceQuestionPresenter(MultipleChoiceQuestionView multipleChoiceQuestionView) {
        mQuestionView = multipleChoiceQuestionView;
        mJSInterface = new QuestionJavascriptInterface(this);
    }

    public void setQuestion(QuizQuestion question) {
        mQuestion = (QuizChoiceQuestion) question;
        userAnswer = "";
    }

    public void setAnswer(String answer) {
        switch (mQuestion.getType()) {
            case MetadataContract.Problems.TYPE_SA:
                if (userAnswer.equals("")) {
                    mQuestionView.onAnswerNotEmpty();
                }
                userAnswer = answer;
                break;
            case MetadataContract.Problems.TYPE_MA:
                if (!userAnswer.contains(answer)) {
                    if (userAnswer.equals("")) {
                        mQuestionView.onAnswerNotEmpty();
                    }
                    userAnswer += answer + ";";
                }
                break;
            default:
                Log.e(MultipleChoiceQuestionPresenter.class.getName(),
                        "Non-multiple-choice problem encountered: " + mQuestion);
        }
    }

    public void unsetAnswer(String answer) {
        switch (mQuestion.getType()) {
            case MetadataContract.Problems.TYPE_SA:
                if (userAnswer.equals(answer)) {
                    userAnswer = "";
                    mQuestionView.onAnswerEmpty();
                }
                break;
            case MetadataContract.Problems.TYPE_MA:
                if (userAnswer.contains(answer)) {
                    userAnswer.replace(answer + ";", "");
                    if (userAnswer.equals("")) {
                        mQuestionView.onAnswerEmpty();
                    }
                }
                break;
            default:
                Log.e(MultipleChoiceQuestionPresenter.class.getName(),
                        "Non-multiple-choice problem encountered: " + mQuestion);
        }
    }

    public boolean isCorrectAnswer(String answer) {
        return mQuestion.getAnswer().contains(answer);
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public boolean isCorrect() {
        String[] answers = mQuestion.getAnswer().split(";");
        for (String answer : answers) {
            if (!userAnswer.contains(answer)) {
                return false;
            }
        }
        return true;
    }

    public QuestionJavascriptInterface getJSInterface() {
        return mJSInterface;
    }
}
