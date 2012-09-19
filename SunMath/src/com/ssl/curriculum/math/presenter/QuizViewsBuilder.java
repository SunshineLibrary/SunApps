package com.ssl.curriculum.math.presenter;

import android.content.Context;
import com.ssl.curriculum.math.component.flipperchildren.QuizChoiceView;
import com.ssl.curriculum.math.component.flipperchildren.QuizFillInView;
import com.ssl.curriculum.math.component.flipperchildren.QuizQuestionView;
import com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.metadata.provider.MetadataContract;

public class QuizViewsBuilder {
    private Context context;
    private QuizPresenter presenter;

    public QuizViewsBuilder(Context context, QuizPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public QuizQuestionView buildQuizView(QuizQuestion question) {
        switch (question.getType()) {
            case MetadataContract.Problems.TYPE_FB:
                return buildFillInView(question);
            case MetadataContract.Problems.TYPE_MC:
            case MetadataContract.Problems.TYPE_SC:
                return buildChoiceView((QuizChoiceQuestion) question);

        }
        return null;
    }

    private QuizChoiceView buildChoiceView(QuizChoiceQuestion question) {
        QuizChoiceView quizChoiceView = createQuizChoiceView(question);
        quizChoiceView.loadQuiz(question);
        quizChoiceView.setQuizPresenter(presenter);
        return quizChoiceView;
    }

    private QuizChoiceView createQuizChoiceView(QuizChoiceQuestion question) {
        return new QuizChoiceView(context, question.getId(), question.getType() == MetadataContract.Problems.TYPE_SC);
    }

    private QuizFillInView buildFillInView(QuizQuestion question) {
        QuizFillInView fillIn = new QuizFillInView(context, question.getId());
        fillIn.setQuizPresenter(presenter);
        fillIn.loadQuiz(question);
        return fillIn;
    }


}
