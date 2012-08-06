package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.QuizLoadedListener;
import com.ssl.curriculum.math.model.activity.QuizDomainData;
import com.ssl.curriculum.math.presenter.QuizPresenter;
import com.ssl.curriculum.math.presenter.QuizViewsBuilder;
import com.ssl.curriculum.math.service.QuizQuestionsProvider;

public class QuizFlipperChild extends LinearLayout implements QuizLoadedListener {
    private TextView nextBtn;
    private ViewFlipper questionFlipper;

    private QuizViewsBuilder quizViewsBuilder;
    private QuizPresenter presenter;

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
        nextBtn = (TextView) findViewById(R.id.quiz_next_button);
        questionFlipper = (ViewFlipper) findViewById(R.id.quiz_question_view);
    }

    private void initListener() {
        nextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                nextQuestion();
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
        questionFlipper.addView(view);
        questionFlipper.showNext();
    }
}