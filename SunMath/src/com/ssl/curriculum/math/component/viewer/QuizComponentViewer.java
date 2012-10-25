package com.ssl.curriculum.math.component.viewer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.component.quiz.*;
import com.ssl.curriculum.math.listener.QuestionResultListener;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;

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

    private QuizComponentView mCurrentComponentView;
    private FlipAnimationManager mAnimationManager;

    private QuizQuestionView mFillBlankView, mMultipleChoiceView;
    private QuizSummaryView mSummaryView;

    public QuizComponentViewer(Context context) {
        super(context);
        mAnimationManager = new FlipAnimationManager(context);
        createResultView();
    }

    public QuizComponentViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAnimationManager = new FlipAnimationManager(context);
        createResultView();
    }

    public QuizComponentViewer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAnimationManager = new FlipAnimationManager(context);
        createResultView();
    }

    public void setControlButtons(ImageView confirmButton, ImageView nextButton) {
        iv_confirmButton = confirmButton;
        iv_nextButton = nextButton;
    }

    public void reset() {
        if (mCurrentComponentView != null) {
            mCurrentComponentView.setVisibility(INVISIBLE);
            mCurrentComponentView = null;
        }
        mQuestions = null;
        mCurrentPosition = -1;
        mSummaryView.reset();
        hideButtons();
    }

    public void setQuestions(List<QuizQuestion> questions) {
        mQuestions = questions;
        mCurrentPosition = -1;
    }

    public void startNextQuestion() {
        if (++mCurrentPosition < mQuestions.size()) {
            startQuestion(mQuestions.get(mCurrentPosition));
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
        mCurrentComponentView.onAfterFlippingIn();
    }

    public void startQuestion(QuizQuestion question) {
        if (mCurrentComponentView != null) {
            mCurrentComponentView.onBeforeFlippingOut();
            flipCurrentQuestionOut();
        }

        setQuestion(question);

        flipCurrentQuestionIn();
        showConfirmButton();
        mCurrentComponentView.onAfterFlippingIn();
    }


    public void setQuestion(QuizQuestion question) {
        QuizQuestionView questionView = getQuestionView(question);
        questionView.setQuestion(question);
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
            case TYPE_SC:
            case TYPE_MC:
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

    private void createResultView() {
        if (mSummaryView == null) {
            mSummaryView = new QuizSummaryView(getContext(), this);
            addView(mSummaryView);
        }
    }

    public void onConfirmButtonClicked() {
        //check if user answered the question or not
        if (((QuizQuestionView) mCurrentComponentView).isQuestionAnswered()) {
            ((QuizQuestionView) mCurrentComponentView).onQuestionAnswered();
            showNextButton();
        }
    }

    public void onNextButtonClicked() {
        startNextQuestion();
    }

    private void showConfirmButton() {
        iv_confirmButton.setVisibility(VISIBLE);
        iv_nextButton.setVisibility(GONE);
    }

    private void showNextButton() {
        iv_confirmButton.setVisibility(GONE);
        iv_nextButton.setVisibility(VISIBLE);
    }

    private void hideButtons() {
        iv_confirmButton.setVisibility(GONE);
        iv_nextButton.setVisibility(GONE);
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
        if (mSummaryView != null) {
            mSummaryView.onQuestionResult(question, answer, isCorrect);
        }
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

}
