package com.ssl.curriculum.math.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.ssl.curriculum.math.model.activity.QuizActivityData;
import com.ssl.curriculum.math.model.activity.quiz.QuizFillBlankQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizMultichoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.metadata.provider.MetadataContract.ProblemChoices;
import com.sunshine.metadata.provider.MetadataContract.Problems;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionsProvider {

    private ContentResolver contentResolver;

    public QuizQuestionsProvider(Context context) {
        contentResolver = context.getContentResolver();
    }
    
    private void loadQuestionChoices(QuizMultichoiceQuestion question, String answer){
    	final String[] columns = new String[]{ProblemChoices._BODY, ProblemChoices._CHOICE};
    	Cursor cursor = contentResolver.query(ProblemChoices.CONTENT_URI, columns,  ProblemChoices._PARENT_ID + " = " + question.getUniqueId(), null, null);
    	if(cursor != null && cursor.moveToFirst())
    		do{
    			int bodyIndex = cursor.getColumnIndex(ProblemChoices._BODY);
    			int choiceIndex = cursor.getColumnIndex(ProblemChoices._CHOICE);
    			
    			question.addChoice(cursor.getString(bodyIndex), cursor.getString(choiceIndex), answer.equals(cursor.getString(choiceIndex)) ? QuizMultichoiceQuestion.CHOICE_CORRECT : QuizMultichoiceQuestion.CHOICE_INCORRECT);
    		}while(cursor.moveToNext());
    }
    
    public void loadQuizQuestions(QuizActivityData quizData) {
        final String[] columns = new String[]{MetadataContract.Problems._ID, MetadataContract.Problems._TYPE , MetadataContract.Problems._TITLE , MetadataContract.Problems._BODY, MetadataContract.Problems._ANSWER};
        Cursor cursor = contentResolver.query(MetadataContract.Problems.CONTENT_URI, columns,  Problems._PARENT_ID + " = " + quizData.getUniqueId(), null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
            	int idIndex = cursor.getColumnIndex(MetadataContract.Problems._ID);
                int bodyIndex = cursor.getColumnIndex(MetadataContract.Problems._BODY);
                int answerIndex = cursor.getColumnIndex(MetadataContract.Problems._ANSWER);
                int titleIndex = cursor.getColumnIndex(MetadataContract.Problems._TITLE);
                int typeIndex = cursor.getColumnIndex(MetadataContract.Problems._TYPE);
                
                QuizQuestion question = null;
                if(Problems.getInternalType(cursor.getString(typeIndex)) == Problems.TYPE_FILLBLANK){
                	question = createFillBlankQuestion(cursor.getInt(idIndex), cursor.getString(titleIndex), cursor.getString(bodyIndex), cursor.getString(answerIndex));
                }else if(Problems.getInternalType(cursor.getString(typeIndex)) == Problems.TYPE_MULTICHOICE){
                	question = createMultichoiceQuestion(cursor.getInt(idIndex), cursor.getString(titleIndex), cursor.getString(bodyIndex));
                	loadQuestionChoices( (QuizMultichoiceQuestion)question , cursor.getString(answerIndex));
                }
                if (question != null) {
                    quizData.addQuestion(question);
                }
            } while (cursor.moveToNext());
        }
    }

    private QuizFillBlankQuestion createFillBlankQuestion(int id, String title, String body, String answer) {
        QuizFillBlankQuestion question = new QuizFillBlankQuestion();
        question.setUniqueId(id);
        question.setAnswer(answer);
        question.setQuizContent(body);
        question.setTitle(title);
        return question;
    }
    
    private QuizMultichoiceQuestion createMultichoiceQuestion(int id, String title, String body){
    	QuizMultichoiceQuestion question = new QuizMultichoiceQuestion();
    	question.initQuestion(title, body);
    	question.setUniqueId(id);
    	return question;
    }
}
