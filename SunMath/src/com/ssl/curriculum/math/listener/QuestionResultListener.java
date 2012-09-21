package com.ssl.curriculum.math.listener;

import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public interface QuestionResultListener {

    public void onQuestionResult(QuizQuestion question, String answer, boolean isCorrect);

}
