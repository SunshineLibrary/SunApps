package com.ssl.curriculum.math.component.viewer;

import android.content.ContentValues;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.component.quiz.*;
import com.ssl.curriculum.math.listener.QuestionResultListener;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.metadata.provider.MetadataContract;

import java.util.List;

import static com.ssl.metadata.provider.MetadataContract.Problems.*;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class QuizComponentViewer extends FrameLayout implements View.OnClickListener, QuestionResultListener{

    private int mCurrentPosition;
    private List<QuizQuestion> mQuestions;

    private ImageView iv_confirmButton, iv_nextButton;
    
    private RelativeLayout rv_result_answer;
    private TextView tv_answerText;
    private ImageView iv_result;
    private TextView tv_answerString;
    private TextView tv_answer;

    private QuizComponentView mCurrentComponentView;
    private FlipAnimationManager mAnimationManager;

    private QuizQuestionView mFillBlankView, mMultipleChoiceView;
    private QuizSummaryView mSummaryView;
    private LinkedActivityData mActivityData;
    private ActivityViewer mActivityViewer;

    private long componentStartTime;
    
    private static final int ANSWER_TEXT_ID = 1000;
    private static final int IMAGE_RESULT_ID = 2000;
    private static final int ANSWER_STRING_ID = 3000;

    public QuizComponentViewer(Context context) {
        super(context);
        mAnimationManager = new FlipAnimationManager(context);
    }

    public QuizComponentViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAnimationManager = new FlipAnimationManager(context);
    }

    public QuizComponentViewer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAnimationManager = new FlipAnimationManager(context);
    }

    public void setControlButtons(ImageView confirmButton, ImageView nextButton) {
        iv_confirmButton = confirmButton;
        iv_nextButton = nextButton;
    }
    
    public void setResultAnswerView(RelativeLayout result_answer, TextView answerText,
    		ImageView result, TextView answerString, TextView answer){
    	rv_result_answer = result_answer;
    	tv_answerText = answerText;
    	iv_result = result;
    	tv_answerString = answerString;
    	tv_answer = answer;
    	
    }

    public void reset() {
        if (mCurrentComponentView != null) {
            mCurrentComponentView.setVisibility(INVISIBLE);
            mCurrentComponentView = null;
        }
        mQuestions = null;
        mActivityData = null;
        mCurrentPosition = -1;
        removeResultView();
        createResultView();
        hideButtons();
        hideResultAnswerView();
    }

    public void setActivityData(LinkedActivityData activityData) {
        mActivityData = activityData;
    }

    public void setResult(String result) {
        if (mActivityData != null) {
            mActivityData.setResult(result);
        }
    }

    public void setQuestions(List<QuizQuestion> questions) {
        mQuestions = questions;
        mCurrentPosition = -1;
    }

    public void startNextQuestion() {
        if (++mCurrentPosition < mQuestions.size()) {
        	int positionNum = mCurrentPosition+1;
            startQuestion(mQuestions.get(mCurrentPosition), positionNum);
            componentStartTime = System.currentTimeMillis() / 1000;
        } else {
            startResult();
        }
    }

    private void startResult() {
        if (mCurrentComponentView != null) {
            mCurrentComponentView.onBeforeFlippingOut();
            flipCurrentQuestionOut();
        }

        mCurrentComponentView = mSummaryView;

        flipCurrentQuestionIn();
        hideButtons();
        hideResultAnswerView();
        mCurrentComponentView.onAfterFlippingIn();
    }

    public void startQuestion(QuizQuestion question, int positionNum) {
        if (mCurrentComponentView != null) {
            mCurrentComponentView.onBeforeFlippingOut();
            flipCurrentQuestionOut();
        }
        //hereLiu:
        setQuestion(question,positionNum);

        flipCurrentQuestionIn();
        hideConfirmButton();
        hideResultAnswerView();
        mCurrentComponentView.onAfterFlippingIn();
    }


    public void setQuestion(QuizQuestion question, int positionNum) {
        QuizQuestionView questionView = getQuestionView(question);
        questionView.setQuestion(question, positionNum);
        mCurrentComponentView = questionView;
    }

    public void flipCurrentQuestionIn() {
        mCurrentComponentView.startAnimation(mAnimationManager.getFlipInFromRightAnimation());
        mCurrentComponentView.setVisibility(VISIBLE);
    }

    public void flipCurrentQuestionOut() {
        mCurrentComponentView.startAnimation(mAnimationManager.getFlipOutToLeftAnimation());
        mCurrentComponentView.setVisibility(INVISIBLE);
    }

    private QuizQuestionView getQuestionView(QuizQuestion question) {
        switch(question.getType()) {
            case TYPE_FB:
                return getFillBlankView();
            case TYPE_SA:
            case TYPE_MA:
                return getMultipleChoiceView();
        }
        return null;
    }

    private QuizQuestionView getFillBlankView() {
        if (mFillBlankView == null) {
            mFillBlankView = new FillBlankQuestionView(getContext(), this);
            addView(mFillBlankView);
        }
        return mFillBlankView;
    }

    private QuizQuestionView getMultipleChoiceView() {
        if (mMultipleChoiceView == null) {
            mMultipleChoiceView = new MultipleChoiceQuestionView(getContext(), this);
            addView(mMultipleChoiceView);
        }
        return mMultipleChoiceView;
    }

    private void removeResultView() {
        if (mSummaryView != null) {
            removeView(mSummaryView);
            mSummaryView = null;
        }
    }

    private void createResultView() {
        if (mSummaryView == null) {
            mSummaryView = new QuizSummaryView(getContext(), this);
            mSummaryView.setVisibility(INVISIBLE);
            addView(mSummaryView);
        }
    }

    public void onConfirmButtonClicked() {
       boolean isCorrected = ((QuizQuestionView) mCurrentComponentView).onQuestionAnswered();
       String answer = mQuestions.get(mCurrentPosition).getAnswer();
       showResultAnswerView(isCorrected,answer);
        //when click the confirm button then decide whether correct and show the answer,the next button
        //isCorrected?answer?
        showNextButton();
    }

    public void onNextButtonClicked() {
        startNextQuestion();
    }

    public void showConfirmButton() {
        iv_confirmButton.setVisibility(VISIBLE);
        iv_nextButton.setVisibility(GONE);
    }

    public void hideConfirmButton() {
        iv_confirmButton.setVisibility(INVISIBLE);
        iv_nextButton.setVisibility(GONE);
    }

    public void showNextButton() {
        iv_confirmButton.setVisibility(GONE);
        iv_nextButton.setVisibility(VISIBLE);
    }

    private void hideButtons() {
        iv_confirmButton.setVisibility(GONE);
        iv_nextButton.setVisibility(GONE);
    }

    private void hideResultAnswerView(){
    	rv_result_answer.setVisibility(GONE);
    	iv_result.setVisibility(GONE);
    	tv_answer.setVisibility(GONE);
    }
    
    private void showResultAnswerView(boolean isCorrected, String answer){
System.out.println("isCorrected:"+isCorrected+"  answer:"+answer);
    	rv_result_answer.removeAllViews();
    	android.view.ViewGroup.LayoutParams basic_params = new android.view.ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	
    	TextView answerText = new TextView(rv_result_answer.getContext());
    	answerText.setId(ANSWER_TEXT_ID);
    	answerText.setText("回答");
    	answerText.setTextSize(20);
    	android.widget.RelativeLayout.LayoutParams tv_answerText_layoutParams = new android.widget.RelativeLayout.LayoutParams(basic_params);
    	tv_answerText_layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
    	tv_answerText_layoutParams.leftMargin = 10;
    	//tv_answerText_layoutParams.setMargins(10, 10, 1, 2);
    	//tv_answerText_layoutParams.addRule(verb);
    	rv_result_answer.addView(answerText, 0, tv_answerText_layoutParams);
    	
    	ImageView iv_result = new ImageView(rv_result_answer.getContext());
    	iv_result.setId(IMAGE_RESULT_ID);
    	if(isCorrected){
    		iv_result.setImageResource(R.drawable.ic_choice_correct);
    	}else{
    		iv_result.setImageResource(R.drawable.ic_choice_incorrect);
    	}
    	android.widget.RelativeLayout.LayoutParams iv_result_layoutParams = new android.widget.RelativeLayout.LayoutParams(basic_params);
    	iv_result_layoutParams.addRule(RelativeLayout.RIGHT_OF, answerText.getId());
    	iv_result_layoutParams.leftMargin = 2;
    	//iv_result_layoutParams.setMargins(60, 10, 10, 2);
    	rv_result_answer.addView(iv_result, 1, iv_result_layoutParams);
    	
    	TextView answerString = new TextView(rv_result_answer.getContext());
    	answerString.setId(ANSWER_STRING_ID);
    	answerString.setText("正确答案是:");
    	answerString.setTextSize(20);
    	android.widget.RelativeLayout.LayoutParams tv_answerString_layoutParams = new android.widget.RelativeLayout.LayoutParams(basic_params);
    	tv_answerString_layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
    	tv_answerString_layoutParams.leftMargin = 10;
    	tv_answerString_layoutParams.addRule(RelativeLayout.BELOW, answerText.getId());
    	tv_answerString_layoutParams.topMargin = 10;
    	//tv_answerString_layoutParams.setMargins(550, 10, 1, 1);
    	rv_result_answer.addView(answerString, 2, tv_answerString_layoutParams);
    	
    	TextView answerView = new TextView(rv_result_answer.getContext());
    	answerView.setText(answer);
    	answerView.setTextSize(25);
    	answerView.setTextColor(R.color.answer);
    	android.widget.RelativeLayout.LayoutParams tv_answer_layoutParams = new android.widget.RelativeLayout.LayoutParams(basic_params);
    	tv_answer_layoutParams.addRule(RelativeLayout.RIGHT_OF, answerString.getId());
    	tv_answer_layoutParams.leftMargin = 2;
    	tv_answer_layoutParams.addRule(RelativeLayout.BELOW, iv_result.getId());
    	tv_answer_layoutParams.topMargin = 1;
    	//tv_answer_layoutParams.setMargins(650, 10, 10, 3);
    	rv_result_answer.addView(answerView, 3, tv_answer_layoutParams);
    	
    	rv_result_answer.setVisibility(VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(iv_confirmButton)) {
            onConfirmButtonClicked();
        } else if (v.equals(iv_nextButton)) {
            onNextButtonClicked();
        }
    }

    @Override
    public void onQuestionResult(QuizQuestion question, String answer, boolean isCorrect) {
        saveQuestionRecord(question.getId(), answer, isCorrect, System.currentTimeMillis() / 1000 - componentStartTime);
        if (mSummaryView != null) {
            mSummaryView.onQuestionResult(question, answer, isCorrect);
        }
    }

    private void saveQuestionRecord(int id, String answer, boolean isCorrect, long duration) {
        ContentValues values = new ContentValues();
        values.put(MetadataContract.Problems._USER_ANSWER, answer);
        values.put(MetadataContract.Problems._IS_CORRECT, isCorrect);
       values.put(MetadataContract.Problems._DURATION, (int) duration);
        getContext().getContentResolver().update(MetadataContract.Problems.getProblemUri(id), values, null, null);
    }

    public void onDestroy() {
        if (mFillBlankView != null) {
            mFillBlankView.onDestroy();
        }
        if (mMultipleChoiceView != null) {
            mMultipleChoiceView.onDestroy();
        }
        if (mSummaryView != null) {
            mSummaryView.onDestroy();
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

    public void onCompleteButtonClicked(View v) {
        mActivityViewer.onNextBtnClicked(v);
    }

    public void setActivityViewer(ActivityViewer activityViewer) {
        mActivityViewer = activityViewer;
    }
}
