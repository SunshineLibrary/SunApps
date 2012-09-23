package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.viewer.QuizComponentViewer;
import com.ssl.curriculum.math.listener.QuestionResultListener;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.presenter.quiz.QuizSummaryViewPresenter;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class QuizSummaryView extends QuizComponentView implements QuestionResultListener{

    private QuizSummaryViewPresenter mSummaryPresenter;

    private ImageView iv_quiz_banner, iv_summary_stars;
    private TextView tv_quiz_score;

    public QuizSummaryView(Context context, QuizComponentViewer quizComponentViewer) {
        super(context, quizComponentViewer);
        initUI();
        initComponents();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_summary_layout, this, false);
        addView(viewGroup);
        iv_quiz_banner = (ImageView) viewGroup.findViewById(R.id.iv_banner);
        iv_summary_stars = (ImageView) viewGroup.findViewById(R.id.iv_summary_stars);
        tv_quiz_score = (TextView) viewGroup.findViewById(R.id.tv_quiz_score);
    }

    private void initComponents() {
        mSummaryPresenter = new QuizSummaryViewPresenter(getContext(), this);
    }


    @Override
    public void onQuestionResult(QuizQuestion question, String answer, boolean isCorrect) {
        mSummaryPresenter.onQuestionResult(question, answer, isCorrect);
    }

    public void reset() {
        setVisibility(INVISIBLE);
        initComponents();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            mSummaryPresenter.onDisplayed();
        }
    }

    public void setScore(String score) {
        tv_quiz_score.setText(score);
    }

    public void setGreat() {
        iv_quiz_banner.setImageResource(R.drawable.ic_quiz_banner_great);
        iv_summary_stars.setImageResource(R.drawable.ic_quiz_three_stars);
    }

    public void setGood() {
        iv_quiz_banner.setImageResource(R.drawable.ic_quiz_banner_good);
        iv_summary_stars.setImageResource(R.drawable.ic_quiz_two_stars);
    }

    public void setTryAgain() {
        iv_quiz_banner.setImageResource(R.drawable.ic_quiz_banner_try_again);
        iv_summary_stars.setImageResource(R.drawable.ic_quiz_one_stars);
    }

    public void onDestroy() {
    }
}
