package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.listener.ProblemLoadedListener;
import com.ssl.curriculum.math.listener.QuizLoadedListener;
import com.ssl.curriculum.math.model.activity.QuizDomainData;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.service.QuizQuestionsProvider;

public class QuizPresenter implements ProblemLoadedListener {
    private QuizLoadedListener loadedListener = null;
    private QuizQuestionsProvider provider;

    private QuizDomainData quizDomainData;
    private int currentPos = 0;

    public QuizPresenter(QuizQuestionsProvider quizQuestionsProvider, QuizDomainData domainData) {
        this.quizDomainData = domainData;
        this.provider = quizQuestionsProvider;
        loadQuizQuestions();
    }

    public void setQuizLoadedListener(QuizLoadedListener quizLoadedListener) {
        this.loadedListener = quizLoadedListener;
    }

    private void loadQuizQuestions() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                provider.loadQuizQuestions(quizDomainData);
                onProblemLoaded();
            }
        }).start();
    }

    public QuizQuestion getQuestion() {
        return quizDomainData.getQuestionById(currentPos++);
    }

    public boolean isToNext() {
        return currentPos < quizDomainData.size();
    }

    public boolean isToFirst() {
        return quizDomainData.size() > 0;
    }

    @Override
    public void onProblemLoaded() {
        if (this.loadedListener != null) {
            this.loadedListener.onQuizLoaded();
        }
    }

    public boolean isCorrect(String userInput, int questionId) {
        boolean result = userInput != null && !userInput.trim().equals(userInput) && userInput.equalsIgnoreCase(getAnswer(questionId));
        if (result) {
            recordResult(questionId);
        }
        return result;
    }

    private void recordResult(int questionId) {
        quizDomainData.getQuestionById(questionId).setPass(true);
    }

    public String getAnswer(int questionId) {
        QuizQuestion questionById = quizDomainData.getQuestionById(questionId);
        return questionById.getAnswer();
    }
}
