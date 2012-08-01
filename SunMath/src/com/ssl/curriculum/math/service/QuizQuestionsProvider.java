package com.ssl.curriculum.math.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import com.ssl.curriculum.math.model.activity.quiz.QuizFillBlankQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.sunshine.metadata.provider.MetadataContract;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionsProvider {

    private ContentResolver contentResolver;

    public QuizQuestionsProvider(Context context) {
        contentResolver = context.getContentResolver();
    }

    public List<QuizQuestion> loadQuizQuestions(String title) {
        List<QuizQuestion> list = new ArrayList<QuizQuestion>();
        final String[] columns = new String[]{MetadataContract.Problems._ID, MetadataContract.Problems._TYPE, MetadataContract.Problems._BODY, MetadataContract.Problems._ANSWER};
        Cursor cursor = contentResolver.query(MetadataContract.Problems.CONTENT_URI, columns, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int bodyIndex = cursor.getColumnIndex(MetadataContract.Problems._BODY);
                int answerIndex = cursor.getColumnIndex(MetadataContract.Problems._ANSWER);
                QuizQuestion question = createQuestion(title, cursor.getString(bodyIndex), cursor.getString(answerIndex));
                if (question != null) {
                    list.add(question);
                }
            } while (cursor.moveToNext());
        }
        return list;
    }

    private QuizQuestion createQuestion(String title, String body, String answer) {
        QuizFillBlankQuestion question = new QuizFillBlankQuestion();
        question.setAnswer(answer);
        question.setQuizContent(body);
        question.setTitle(title);
        return question;
    }
}
