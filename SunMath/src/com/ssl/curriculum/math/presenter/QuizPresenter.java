package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.component.flipperchildren.subviews.QuizFillInSubview;
import com.ssl.curriculum.math.listener.ProblemLoadedListener;
import com.ssl.curriculum.math.listener.QuizLoadedListener;
import com.ssl.curriculum.math.model.activity.QuizDomainActivityData;
import com.ssl.curriculum.math.model.activity.quiz.QuizFillBlankQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.service.QuizQuestionsProvider;

public class QuizPresenter implements ProblemLoadedListener {
    private QuizLoadedListener loadedListener = null;
    private QuizDomainActivityData quizData;
    private QuizQuestionsProvider provider;
    private QuizQuestion currentQuestion;
    private int currentPos = 0;
    private QuizFillInSubview quizFillInSubview;

    public QuizPresenter(QuizDomainActivityData qad, QuizQuestionsProvider qqp) {
        this.quizData = qad;
        this.provider = qqp;
        loadQuizQuestions();
    }

    private void setQuizLoadedListener(QuizLoadedListener l) {
        this.loadedListener = l;
    }

    private void loadQuizQuestions() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                provider.loadQuizQuestions(quizData);
                onProblemLoaded();
            }
        }).start();
    }

    public QuizQuestion getQuestion() {
        return this.currentQuestion;
    }

    public boolean toNext() {
        if ((this.currentPos + 1) < quizData.size()) {
            this.currentPos++;
            this.currentQuestion = this.quizData.getQuestion(this.currentPos);
            System.out.println("currentQuestion = " + currentQuestion + ", " + currentPos);
            return true;
        }
        return false;
    }

    public boolean toFirst() {
        this.currentPos = 0;
        if (this.quizData.size() > 0) {
            this.currentQuestion = this.quizData.getQuestion(this.currentPos);
            return true;
        } else {
            return false;
        }
    }

    public boolean getCurrentAnswerState(String userInput) {
        if (userInput.equals(null) || this.getCurrentAnswer().equals(null))
            return false;
        return userInput.equals(this.getCurrentAnswer());
    }

    public String getCurrentAnswer() {
        if (quizData == null || quizData.size() == 0 || currentPos < 0 || currentPos >= quizData.size())
            return null;
        return ((QuizFillBlankQuestion) quizData.getQuestion(currentPos)).getAnswer();
    }

    @Override
    public void onProblemLoaded() {
        if (this.loadedListener != null) {
            this.loadedListener.onQuizLoaded();
        }
    }
}
