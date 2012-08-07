package com.ssl.curriculum.math.presenter;

import android.content.Context;
import com.ssl.curriculum.math.component.flipperchildren.QuizFillInView;
import com.ssl.curriculum.math.component.flipperchildren.QuizChoiceView;
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
            case QuizQuestion.TYPE_CHOICE:
                return buildChoiceView(question);
            case QuizQuestion.TYPE_FILLBLANKS:
                return buildFillInView((QuizFillBlankQuestion) question);
        }
        return null;
    }

    private QuizChoiceView buildChoiceView(QuizQuestion question) {
        QuizChoiceView quizChoiceView = createQuizChoiceView(question);
        quizChoiceView.loadQuiz("");
        quizChoiceView.setQuizPresenter(presenter);
        return quizChoiceView;
    }

    private QuizChoiceView createQuizChoiceView(QuizQuestion question) {
        return new QuizChoiceView(context, question.getId(), true);
    }

    private QuizFillInView buildFillInView(QuizFillBlankQuestion question) {
        QuizFillInView fillIn = new QuizFillInView(context, question.getId());
        fillIn.setQuizPresenter(presenter);
        fillIn.loadQuiz(question.getQuizContent());
        return fillIn;
    }


}
