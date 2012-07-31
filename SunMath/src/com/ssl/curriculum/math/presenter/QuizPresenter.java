package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.component.flipperchildren.QuizFillInFlipperChild;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.service.QuizQuestionsProvider;

import java.util.List;

public class QuizPresenter {

    private List<QuizQuestion> quizQuestions;
    private QuizFillInFlipperChild quizFillInFlipperChild;

    public QuizPresenter(QuizFillInFlipperChild quizFillInFlipperChild) {
        this.quizFillInFlipperChild = quizFillInFlipperChild;
    }

    public void loadQuizQuestions(final QuizQuestionsProvider quizQuestionsProvider, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                quizQuestions = quizQuestionsProvider.loadQuizQuestions(title);
                updateQuizView();
            }
        });
    }

    private void updateQuizView() {
        if(quizQuestions == null || quizQuestions.size() == 0 )return;
        quizFillInFlipperChild.loadQuiz(quizQuestions.get(0));
    }
}
