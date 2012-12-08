package com.ssl.curriculum.math.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.utils.IterableCursor;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.metadata.provider.MetadataContract.ProblemChoices;

import java.util.ArrayList;
import java.util.List;

import static com.ssl.metadata.provider.MetadataContract.Problems;

public class QuizQuestionsLoader {

    private ContentResolver mResolver;

    public QuizQuestionsLoader(Context context) {
        mResolver = context.getContentResolver();
    }

    public List<QuizQuestion> getQuizQuestions(LinkedActivityData quizData) {
        if (quizData.type == MetadataContract.Activities.TYPE_QUIZ) {
            List<QuizQuestion> questions = new ArrayList<QuizQuestion>();
            String orderBy = MetadataContract.QuizComponents._SEQUENCE;
            Cursor cursor = mResolver.query(MetadataContract.QuizComponents.PROBLEMS_URI, null,
                    MetadataContract.QuizComponents._QUIZ_ACTIVITY_ID + "=" + quizData.activityId, null, orderBy);

            for (Cursor c : new IterableCursor(cursor)) {
                int id = cursor.getInt(cursor.getColumnIndex(Problems._ID));
                int type = cursor.getInt(cursor.getColumnIndex(Problems._TYPE));
                String body = cursor.getString(cursor.getColumnIndex(Problems._BODY));
                String answer = cursor.getString(cursor.getColumnIndex(Problems._ANSWER));
                int quiz_num = quizData.getQuizNum();
                questions.add(createNewQuizQuestion(body, answer, id, type, quiz_num));
            }
            loadMultiChoices(questions);

            return questions;
        } else {
            return null;
        }
    }

    private QuizQuestion createNewQuizQuestion(String body, String answer, int id, int type, int quiz_num) {
        QuizQuestion question = new QuizQuestion(body, answer, id, type, quiz_num);
        if (type != Problems.TYPE_FB) {
            question = new QuizChoiceQuestion(question);
        }
        return question;
    }

    private void loadMultiChoices(List<QuizQuestion> quizQuestions) {
        Cursor cursor = mResolver.query(MetadataContract.ProblemChoices.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int parentIdIndex = cursor.getColumnIndex(ProblemChoices._PARENT_ID);
                int bodyIndex = cursor.getColumnIndex(MetadataContract.ProblemChoices._BODY);
                int choiceIndex = cursor.getColumnIndex(ProblemChoices._CHOICE);

                int parentId = cursor.getInt(parentIdIndex);
                QuizQuestion problemContainsChoice = getProblemContainsChoice(quizQuestions, parentId);
                if (problemContainsChoice == null) {
                    continue;
                }
                QuizChoiceQuestion quizChoiceQuestion = (QuizChoiceQuestion) problemContainsChoice;
                quizChoiceQuestion.addChoice(new QuizChoiceQuestion.Choice(cursor.getString(choiceIndex), cursor.getString(bodyIndex)));
            } while (cursor.moveToNext());
        }
    }

    private QuizQuestion getProblemContainsChoice(List<QuizQuestion> quizQuestions, int parentId) {
        for (QuizQuestion quizQuestion : quizQuestions) {
            if (quizQuestion.getType() != Problems.TYPE_FB && quizQuestion.getId() == parentId) {
                return quizQuestion;
            }
        }
        return null;
    }

}
