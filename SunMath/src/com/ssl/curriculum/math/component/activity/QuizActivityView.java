package com.ssl.curriculum.math.component.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;
import com.ssl.curriculum.math.component.viewer.QuestionViewer;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.service.QuizQuestionsLoader;

import java.util.List;

public class QuizActivityView extends ActivityView {
    private ImageView nextBtn;
    private ImageView confirmBtn;
    private QuizQuestionsLoader mQuestionLoader;

    private QuestionViewer mQuestionViewer;

    public QuizActivityView(Context context, ActivityViewer activityViewer) {
        super(context, activityViewer);
        initUI();
        initComponents();
        initListener();
    }

    @Override
    public void setActivity(LinkedActivityData activityData) {
        super.setActivity(activityData);
        mQuestionViewer.reset();
        new LoadQuestionTask(activityData).execute();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.quiz_flip_layout, this, false);
        addView(viewGroup);
        nextBtn = (ImageView) findViewById(R.id.quiz_next_btn);
        confirmBtn = (ImageView) findViewById(R.id.quiz_choice_ok_btn);
        mQuestionViewer = (QuestionViewer) findViewById(R.id.quiz_question_view);
    }

    private void initComponents() {
        mQuestionLoader = new QuizQuestionsLoader(this.getContext());
    }

    private void initListener() {
        mQuestionViewer.setControlButtons(confirmBtn, nextBtn);
        nextBtn.setOnClickListener(mQuestionViewer);
        confirmBtn.setOnClickListener(mQuestionViewer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mQuestionViewer.onDestroy();
    }

    private class LoadQuestionTask extends AsyncTask {
        private LinkedActivityData mActivityData;
        private List<QuizQuestion> mQuestions;

        public LoadQuestionTask(LinkedActivityData activityData) {
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
            mQuestionViewer.setQuestions(mQuestions);
            mQuestionViewer.startNextQuestion();
        }
    }
}