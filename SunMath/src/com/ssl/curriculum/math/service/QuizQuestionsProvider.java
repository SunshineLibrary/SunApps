package com.ssl.curriculum.math.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.ssl.curriculum.math.model.activity.QuizDomainData;
import com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.metadata.provider.MetadataContract.ProblemChoices;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionsProvider {

    private ContentResolver contentResolver;

    public QuizQuestionsProvider(Context context) {
        contentResolver = context.getContentResolver();
    }

    public void loadQuizQuestions(QuizDomainData quizData) {
        final String[] columns = new String[]{MetadataContract.QuizComponents._PROBLEM_ID};
        List<Integer> allProblemIds = new ArrayList<Integer>();
        Cursor cursor = contentResolver.query(MetadataContract.QuizComponents.getProblemToQuizActivityUri(quizData.activityId), columns, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int problemIdIndex = cursor.getColumnIndex(MetadataContract.QuizComponents._PROBLEM_ID);
                allProblemIds.add(cursor.getInt(problemIdIndex));
            } while (cursor.moveToNext());
        }
        Log.i("fetch problems id", allProblemIds.toString());

        fetchAllProblems(allProblemIds);
    }

    private void fetchAllProblems(List<Integer> allProblemIds) {
        List<QuizQuestion> quizQuestions = new ArrayList<QuizQuestion>();
        final String[] columns = new String[]{MetadataContract.Problems._ID, MetadataContract.Problems._BODY, MetadataContract.Problems._TYPE, MetadataContract.Problems._ANSWER};
        Cursor cursor = contentResolver.query(MetadataContract.Problems.CONTENT_URI, columns, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(MetadataContract.Problems._ID);
                int bodyIndex = cursor.getColumnIndex(MetadataContract.Problems._BODY);
                int typeIndex = cursor.getColumnIndex(MetadataContract.Problems._TYPE);
                int answerIndex = cursor.getColumnIndex(MetadataContract.Problems._ANSWER);
                int id = cursor.getInt(idIndex);
                if (!allProblemIds.contains(id)) {
                    continue;
                }
                quizQuestions.add(new QuizQuestion(cursor.getString(bodyIndex), cursor.getString(answerIndex),
                        id, cursor.getInt(typeIndex)));
            } while (cursor.moveToNext());
        }

        loadMultiChoices(quizQuestions);
        Log.i("load all problems", quizQuestions.toString());
    }

    private void loadMultiChoices(List<QuizQuestion> quizQuestions) {
        final String[] columns = new String[]{ProblemChoices._PARENT_ID, MetadataContract.ProblemChoices._BODY, ProblemChoices._CHOICE};
        Cursor cursor = contentResolver.query(MetadataContract.ProblemChoices.CONTENT_URI, columns, null, null, null);
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
                QuizChoiceQuestion quizChoiceQuestion = new QuizChoiceQuestion(problemContainsChoice);
                quizChoiceQuestion.addChoice(new QuizChoiceQuestion.Choice(cursor.getString(choiceIndex), cursor.getString(bodyIndex)));

                quizQuestions.remove(problemContainsChoice);
                quizQuestions.add(quizChoiceQuestion);

            } while (cursor.moveToNext());
        }
    }

    private QuizQuestion getProblemContainsChoice(List<QuizQuestion> quizQuestions, int parentId) {
        for (QuizQuestion quizQuestion : quizQuestions) {
            if (quizQuestion.getType() != MetadataContract.Problems.TYPE_FB && quizQuestion.getId() == parentId) {
                return quizQuestion;
            }
        }
        return null;
    }

}
