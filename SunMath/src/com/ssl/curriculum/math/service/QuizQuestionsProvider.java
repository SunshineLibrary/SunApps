package com.ssl.curriculum.math.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.ssl.curriculum.math.model.activity.QuizDomainData;
import com.ssl.curriculum.math.model.activity.quiz.QuizFillBlankQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizMultiChoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.metadata.provider.MetadataContract.ProblemChoices;

public class QuizQuestionsProvider {

    private ContentResolver contentResolver;

    public QuizQuestionsProvider(Context context) {
        contentResolver = context.getContentResolver();
    }
    
    private void loadQuestionChoices(QuizMultiChoiceQuestion question, String answer){
    	final String[] columns = new String[]{ProblemChoices._BODY, ProblemChoices._CHOICE};
    	Cursor cursor = contentResolver.query(ProblemChoices.CONTENT_URI, columns, null, null, null);
    	if(cursor != null && cursor.moveToFirst())
    		do{
    			int bodyIndex = cursor.getColumnIndex(ProblemChoices._BODY);
    			int choiceIndex = cursor.getColumnIndex(ProblemChoices._CHOICE);
    			
    			question.addChoice(cursor.getString(bodyIndex), cursor.getString(choiceIndex), answer.equals(cursor.getString(choiceIndex)) ? QuizMultiChoiceQuestion.CHOICE_CORRECT : QuizMultiChoiceQuestion.CHOICE_INCORRECT);
    		}while(cursor.moveToNext());
    }
    
    public void loadQuizQuestions(QuizDomainData quizData) {
        final String[] columns = new String[]{MetadataContract.Problems._ID, MetadataContract.Problems._TYPE, MetadataContract.Problems._BODY, MetadataContract.Problems._ANSWER};
        Cursor cursor = contentResolver.query(MetadataContract.Problems.CONTENT_URI, columns,  null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
            	int idIndex = cursor.getColumnIndex(MetadataContract.Problems._ID);
                int bodyIndex = cursor.getColumnIndex(MetadataContract.Problems._BODY);
                int answerIndex = cursor.getColumnIndex(MetadataContract.Problems._ANSWER);
                int typeIndex = cursor.getColumnIndex(MetadataContract.Problems._TYPE);


                QuizQuestion question = null;
                String string = cursor.getString(bodyIndex);
                System.out.println("------------string = " + string);
                question = createFillBlankQuestion(cursor.getInt(idIndex), string, cursor.getString(answerIndex));
//                if(Problems.getInternalType(cursor.getString(typeIndex)) == Problems.TYPE_FILLBLANK){
//                }else if(Problems.getInternalType(cursor.getString(typeIndex)) == Problems.TYPE_MULTICHOICE){
//                	question = createMultichoiceQuestion(cursor.getInt(idIndex),cursor.getString(bodyIndex));
//                	loadQuestionChoices( (QuizMultichoiceQuestion)question , cursor.getString(answerIndex));
//                }
                if (question != null) {
                    quizData.addQuestion(question);
                }
            } while (cursor.moveToNext());
        }
    }

    private QuizFillBlankQuestion createFillBlankQuestion(int id, String body, String answer) {
        QuizFillBlankQuestion question = new QuizFillBlankQuestion();
        question.setId(id);
        question.setAnswer(answer);
        question.setQuizContent(body);
        return question;
    }
    
    private QuizMultiChoiceQuestion createMultichoiceQuestion(int id, String body){
    	QuizMultiChoiceQuestion question = new QuizMultiChoiceQuestion();
    	question.initQuestion(body);
    	question.setId(id);
    	return question;
    }
}
