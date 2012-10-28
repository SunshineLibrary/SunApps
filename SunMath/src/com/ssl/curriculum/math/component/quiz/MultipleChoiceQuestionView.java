package com.ssl.curriculum.math.component.quiz;

import android.content.Context;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.viewer.QuizComponentViewer;
import com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.presenter.quiz.MultipleChoiceQuestionPresenter;

import java.util.List;

import static com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion.Choice;
import static com.ssl.metadata.provider.MetadataContract.Problems.TYPE_MA;
import static com.ssl.metadata.provider.MetadataContract.Problems.TYPE_SA;

public class MultipleChoiceQuestionView extends QuizQuestionView {

    private static final String DIV_FORMAT = "<div class=\"%s\">%s</div>";
    private static final String RADIO_FORMAT =
            "<input type=\"radio\" id=\"%s\" name=\"answer\" value=\"%s\" class=\"choice\"/>" +
                    "<label for=\"%s\">%s %s</label>";
    private static final String CHECKBOX_FORMAT =
            "<input type=\"checkbox\" id=\"%s\" name=\"answer[]\" value=\"%s\" class=\"choice\"/>" +
                    "<label for=\"%s\">%s %s</label>";


    private MultipleChoiceQuestionPresenter mPresenter;
    private ViewGroup viewGroup;
    private QuizChoiceQuestion mQuestion;
    private Handler uiHandler;

    public MultipleChoiceQuestionView(Context context, QuizComponentViewer quizComponentViewer) {
        super(context, quizComponentViewer);
        mPresenter = new MultipleChoiceQuestionPresenter(this);
        uiHandler = new Handler();
    }

    @Override
    public void setQuestion(QuizQuestion question) {
        mQuestion = (QuizChoiceQuestion) question;
        mPresenter.setQuestion(question);
        questionWebView.clearView();
        loadQuizHtml(getQuizContent());
        questionWebView.addJavascriptInterface(mPresenter.getJSInterface(), "Question");
    }

    @Override
    protected String getQuizContent() {
        return getBodyHtml() + getChoicesHtml();
    }

    protected void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_choice_flip_layout, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_choice_flipper_child_question);
    }

    @Override
    public void onQuestionAnswered() {
        questionWebView.loadUrl("javascript:onSubmitAnswer()");
        mQuizComponentViewer.onQuestionResult(mQuestion, mPresenter.getUserAnswer(), mPresenter.isCorrect());
    }

    public void onAnswerNotEmpty() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                mQuizComponentViewer.showConfirmButton();
            }
        });
    }

    public void onAnswerEmpty() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                mQuizComponentViewer.hideConfirmButton();
            }
        });
    }

    private String getBodyHtml() {
        return String.format(DIV_FORMAT, "question", mQuestion.getQuizContent());
    }

    private String getChoicesHtml() {
        List<Choice> choices = mQuestion.getChoices();
        int type = mQuestion.getType();

        String html = "";
        for (int i = 0; i < choices.size(); i++) {
            html += getChoiceHtml(choices.get(i), "choice" + i ,type);
        }

        return String.format(DIV_FORMAT, "choices", html);
    }

    private String getChoiceHtml(Choice choice, String htmlId, int type) {
        switch (type) {
            case TYPE_SA:
                return String.format(RADIO_FORMAT, htmlId, choice.choice, htmlId, choice.choice, choice.body);
            case TYPE_MA:
                return String.format(CHECKBOX_FORMAT, htmlId, choice.choice, htmlId, choice.choice, choice.body);
            default:
                return "";
        }
    }

}
