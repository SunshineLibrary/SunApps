package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.viewer.QuizComponentViewer;
import com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.presenter.quiz.MultipleChoiceQuestionPresenter;
import com.ssl.curriculum.math.utils.StringUtils;

import java.util.List;

import static com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion.Choice;
import static com.ssl.metadata.provider.MetadataContract.Problems.TYPE_MA;
import static com.ssl.metadata.provider.MetadataContract.Problems.TYPE_SA;

public class MultipleChoiceQuestionView extends QuizQuestionView {

    private static final String DIV_FORMAT = "<div class=\"%s\">%s</div>";
    private static final String RADIO_FORMAT =
            "<input type=\"radio\" id=\"%s\" name=\"answer\" value=\"%s\" class=\"choice\"/>" +
                    "<label for=\"%s\">%s) %s</label>";
    private static final String CHECKBOX_FORMAT =
            "<input type=\"checkbox\" id=\"%s\" name=\"answer[]\" value=\"%s\" class=\"choice\"/>" +
                    "<label for=\"%s\">%s) %s</label>";


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
    public void setQuestion(QuizQuestion question, int positionNum) {
        mQuestion = (QuizChoiceQuestion) question;
        mPresenter.setQuestion(question);
        resetView();
        int quiz_num = question.getQuizNum();
        String quizType = "";
        questionWebView.addJavascriptInterface(mPresenter.getJSInterface(), "Question");
        
        //这里已经给出选择题类型的描述了
        switch (mQuestion.getType()) {
            case TYPE_MA://多项选择的判断方式可能没有改，因此找到哪里是对答案的对错做出判断的地方
                questionTitle.setText(R.string.multiple_answer_multiple_choice);
                quizType = questionTitle.getText().toString();
                break;
            case TYPE_SA:
                questionTitle.setText(R.string.single_answer_multiple_choice);
                quizType = questionTitle.getText().toString();
                break;
        }
        
        String questionNum = quiz_num+"."+positionNum+"("+quizType+")";
        //hereLiu:你这里面现在没有选项！在loadquestion的时候对于选择题单独load的有选项的
        loadQuizHtml(getQuizContent(), questionNum);
    }

    @Override
    protected String getQuizContent() {
    	//String imageStr = "这是个简单的测试，包含图片<IMG src=\"file:///android_asset/ladder-shaped.jpg\">";
        return getBodyHtml() + getChoicesHtml();//between body and choice is the picture
    }

    protected void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_choice_flip_layout, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_choice_flipper_child_question);
        progressBar = (ProgressBar) findViewById(R.id.quiz_choice_progress_bar);
        questionTitle = (TextView) findViewById(R.id.quiz_choice_flipper_child_title);
    }

    @Override
    public boolean onQuestionAnswered() {
        questionWebView.loadUrl("javascript:onSubmitAnswer()");
        boolean isCorrected = mPresenter.isCorrect();
        mQuizComponentViewer.onQuestionResult(mQuestion, mPresenter.getUserAnswer(), isCorrected);
        return isCorrected;
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
    	String quizContent = StringUtils.Transcoding2Html(mQuestion.getQuizContent());
        return String.format(DIV_FORMAT, "question", quizContent);
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
    	String choiceBody = StringUtils.Transcoding2Html(choice.body);
        switch (type) {
            case TYPE_SA:
                return String.format(RADIO_FORMAT, htmlId, choice.choice, htmlId, choice.choice, choiceBody);
            case TYPE_MA:
                return String.format(CHECKBOX_FORMAT, htmlId, choice.choice, htmlId, choice.choice, choiceBody);
            default:
                return "";
        }
    }

}
