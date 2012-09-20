package com.ssl.curriculum.math.component.viewer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.component.quiz.FillBlankQuestionView;
import com.ssl.curriculum.math.component.quiz.MultipleChoiceQuestionView;
import com.ssl.curriculum.math.component.quiz.QuizQuestionView;

import java.util.List;

import static com.ssl.metadata.provider.MetadataContract.Problems.*;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class QuestionViewer extends FrameLayout implements View.OnClickListener {

    private int mCurrentPosition;
    private List<QuizQuestion> mQuestions;

    private ImageView iv_confirmButton, iv_nextButton;

    private QuizQuestionView mCurrentQuestionView;
    private FlipAnimationManager mAnimationManager;

    private QuizQuestionView mFillBlankView, mMultipleChoiceView, mResultView;

    public QuestionViewer(Context context) {
        super(context);
        mAnimationManager = new FlipAnimationManager(context);
    }

    public QuestionViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAnimationManager = new FlipAnimationManager(context);
    }

    public QuestionViewer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAnimationManager = new FlipAnimationManager(context);
    }


    public void setControlButtons(ImageView confirmButton, ImageView nextButton) {
        iv_confirmButton = confirmButton;
        iv_nextButton = nextButton;
    }

    public void reset() {
        if (mCurrentQuestionView != null) {
            mCurrentQuestionView.setVisibility(INVISIBLE);
            mCurrentQuestionView = null;
        }
        mQuestions = null;
        mCurrentPosition = -1;
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
        if (mCurrentQuestionView != null) {
            mCurrentQuestionView.onBeforeFlippingOut();
            flipCurrentQuestionOut();
        }

        mCurrentQuestionView = getResultView();

        flipCurrentQuestionIn();
        hideButtons();
        mCurrentQuestionView.onAfterFlippingIn();
    }

    public void startQuestion(QuizQuestion question) {
        if (mCurrentQuestionView != null) {
            mCurrentQuestionView.onBeforeFlippingOut();
            flipCurrentQuestionOut();
        }

        setQuestion(question);

        flipCurrentQuestionIn();
        showConfirmButton();
        mCurrentQuestionView.onAfterFlippingIn();
    }


    public void setQuestion(QuizQuestion question) {
        mCurrentQuestionView = getQuestionView(question);
        mCurrentQuestionView.setQuestion(question);
    }

    public void flipCurrentQuestionIn() {
        mCurrentQuestionView.startAnimation(mAnimationManager.getFlipInFromRightAnimation());
        mCurrentQuestionView.setVisibility(VISIBLE);
    }

    public void flipCurrentQuestionOut() {
        mCurrentQuestionView.startAnimation(mAnimationManager.getFlipOutToLeftAnimation());
        mCurrentQuestionView.setVisibility(INVISIBLE);
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

    public QuizQuestionView getFillBlankView() {
        if (mFillBlankView == null) {
            mFillBlankView = new FillBlankQuestionView(getContext(), this);
            addView(mFillBlankView);
        }
        return mFillBlankView;
    }

    public QuizQuestionView getMultipleChoiceView() {
        if (mMultipleChoiceView == null) {
            mMultipleChoiceView = new MultipleChoiceQuestionView(getContext(), this);
            addView(mMultipleChoiceView);
        }
        return mMultipleChoiceView;
    }

    public QuizQuestionView getResultView() {
        return mResultView;
    }

    public void onConfirmButtonClicked() {
        mCurrentQuestionView.onQuestionAnswered();
        showNextButton();
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

    public void onDestroy() {
        if (mFillBlankView != null) {
            mFillBlankView.onDestroy();
        }
        if (mMultipleChoiceView != null) {
            mMultipleChoiceView.onDestroy();
        }
        if (mResultView != null) {
            mResultView.onDestroy();
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }
}
