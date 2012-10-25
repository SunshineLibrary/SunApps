package com.ssl.curriculum.math.component.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;
import com.ssl.curriculum.math.component.viewer.QuizComponentViewer;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.service.QuizQuestionsLoader;

import java.util.List;

public class QuizActivityView extends ActivityView {
    private ImageView nextBtn;
    private ImageView confirmBtn;
    private QuizQuestionsLoader mQuestionLoader;

    private QuizComponentViewer mQuizComponentViewer;

    public QuizActivityView(Context context, ActivityViewer activityViewer) {
        super(context, activityViewer);
        initUI();
        initComponents();
        initListener();
    }

    @Override
    public void setActivity(LinkedActivityData activityData) {
        super.setActivity(activityData);
        mQuizComponentViewer.reset();
        mQuizComponentViewer.setActivityData(activityData);
        new LoadQuestionsTask(activityData).execute();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.quiz_flip_layout, this, false);
        addView(viewGroup);
        nextBtn = (ImageView) findViewById(R.id.quiz_next_btn);
        confirmBtn = (ImageView) findViewById(R.id.quiz_choice_ok_btn);
        mQuizComponentViewer = (QuizComponentViewer) findViewById(R.id.quiz_question_view);
    }

    private void initComponents() {
        mQuestionLoader = new QuizQuestionsLoader(this.getContext());
    }

    private void initListener() {
        mQuizComponentViewer.setControlButtons(confirmBtn, nextBtn);
        nextBtn.setOnClickListener(mQuizComponentViewer);
        confirmBtn.setOnClickListener(mQuizComponentViewer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mQuizComponentViewer.onDestroy();
    }

    private class LoadQuestionsTask extends AsyncTask {
        private LinkedActivityData mActivityData;
        private List<QuizQuestion> mQuestions;

        public LoadQuestionsTask(LinkedActivityData activityData) {
            mActivityData = activityData;
        }

        @Override
        protected Object doInBackground(Object... params) {
            mQuestions = mQuestionLoader.getQuizQuestions(mActivityData);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mQuizComponentViewer.setQuestions(mQuestions);
            mQuizComponentViewer.startNextQuestion();
        }
    }
}