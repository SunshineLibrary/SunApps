package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.QuizLoadedListener;
import com.ssl.curriculum.math.model.activity.QuizDomainData;
import com.ssl.curriculum.math.presenter.QuizPresenter;
import com.ssl.curriculum.math.presenter.QuizViewsBuilder;
import com.ssl.curriculum.math.service.QuizQuestionsProvider;

public class QuizFlipperChild extends FlipperChildView implements QuizLoadedListener {
    private ImageView nextBtn;
    private ViewFlipper questionFlipper;

    private QuizViewsBuilder quizViewsBuilder;
    private QuizPresenter presenter;
    private ImageView confirmBtn;

    public QuizFlipperChild(Context context, QuizDomainData quiz) {
        super(context);
        initUI();
        initComponents(quiz);
        initListener();
    }

    private void initComponents(QuizDomainData quiz) {
        presenter = new QuizPresenter(new QuizQuestionsProvider(getContext()), quiz);
        quizViewsBuilder = new QuizViewsBuilder(getContext(), presenter);
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.quiz_flip_layout, this, false);
        addView(viewGroup);
        nextBtn = (ImageView) findViewById(R.id.quiz_next_btn);
        confirmBtn = (ImageView) findViewById(R.id.quiz_choice_ok_btn);
        questionFlipper = (ViewFlipper) findViewById(R.id.quiz_question_view);
    }

    private void initListener() {
        nextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                nextQuestion();
            }
        });
        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizQuestionView quizQuestionView = (QuizQuestionView) questionFlipper.getChildAt(questionFlipper.getDisplayedChild());
                quizQuestionView.onQuestionFinished();
                toggleToNextButton();
            }
        });
        presenter.setQuizLoadedListener(this);
    }

    public void nextQuestion() {
        if (presenter.isToNext()) {
            addQuizView();
        }
    }

    @Override
    public void onQuizLoaded() {
        if (presenter.isToFirst()) {
            post(new Runnable() {
                @Override
                public void run() {
                    addQuizView();
                }
            });
        }
    }

    private void addQuizView() {
        QuizQuestionView view = quizViewsBuilder.buildQuizView(presenter.getQuestion());
        if (view == null) return;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        questionFlipper.addView(view, layoutParams);
        questionFlipper.showNext();
        toggleToConfirmBtn();
    }

    private void toggleToNextButton() {
        confirmBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.VISIBLE);
    }

    private void toggleToConfirmBtn() {
        confirmBtn.setVisibility(View.VISIBLE);
        nextBtn.setVisibility(View.GONE);
    }
}