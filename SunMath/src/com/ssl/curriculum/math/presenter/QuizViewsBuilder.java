package com.ssl.curriculum.math.presenter;

import android.content.Context;
import com.ssl.curriculum.math.component.flipperchildren.QuizFillInView;
import com.ssl.curriculum.math.component.flipperchildren.QuizMultiChoiceView;
import com.ssl.curriculum.math.component.flipperchildren.QuizQuestionView;
import com.ssl.curriculum.math.model.activity.quiz.QuizFillBlankQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;

public class QuizViewsBuilder {
    private Context context;
    private QuizPresenter presenter;

    public QuizViewsBuilder(Context context, QuizPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public QuizQuestionView buildQuizView(QuizQuestion question) {
        switch (question.getType()) {
            case QuizQuestion.TYPE_MULTICHOICE:
                return buildMultiView(question.getId());
            case QuizQuestion.TYPE_FILLBLANKS:
                return buildFillInView((QuizFillBlankQuestion) question);
        }
        return null;
    }

    private QuizMultiChoiceView buildMultiView(int questionId) {
        QuizMultiChoiceView quizMultiChoiceView = new QuizMultiChoiceView(context, questionId);
        quizMultiChoiceView.loadQuiz("");
        return quizMultiChoiceView;
    }

    private QuizFillInView buildFillInView(QuizFillBlankQuestion question) {
        QuizFillInView fillIn = new QuizFillInView(context, question.getId());
        fillIn.setQuizPresenter(presenter);
        fillIn.loadQuiz(question.getQuizContent());
        return fillIn;
    }


}
