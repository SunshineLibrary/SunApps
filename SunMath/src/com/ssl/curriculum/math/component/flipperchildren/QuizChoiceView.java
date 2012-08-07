package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.ChoiceTableItemView;
import com.ssl.curriculum.math.component.ChoiceTableView;
import com.ssl.curriculum.math.listener.MultiChoiceJSInterface;

public class QuizChoiceView extends QuizQuestionView {

    private ChoiceTableView choiceTableView;

    public QuizChoiceView(Context context, int questionId, boolean isSingleChoice) {
        super(context, questionId);
        initUI(isSingleChoice);
        initWebView();
        initListeners();
    }

    private void initUI(boolean isSingleChoice) {
        if (isSingleChoice) {
            initUI(R.layout.quiz_single_choice_flip_layout);
        } else {
            initUI(R.layout.quiz_multi_choice_flip_layout);
        }
    }

    private void initUI(int layoutResource) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(layoutResource, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_choice_flipper_child_question);
        choiceTableView = (ChoiceTableView) findViewById(R.id.quiz_choice_table);
        confirmButton = (ImageView) findViewById(R.id.quiz_choice_ok_btn);
    }

    @Override
    protected void initWebView() {
        super.initWebView();
        questionWebView.addJavascriptInterface(new MultiChoiceJSInterface(), "multiChoice");
    }

    @Override
    protected void onQuestionFinished() {
        choiceTableView.checkAnswer(presenter.getAnswer(getQuestionId()));
    }

    @Override
    protected void loadQuizHtml(String quizContent) {
        questionWebView.loadUrl("file:///android_asset/multichoice.html");
        choiceTableView.addChoiceTableRow(new ChoiceTableItemView(getContext(), "test", "A"));
        choiceTableView.addChoiceTableRow(new ChoiceTableItemView(getContext(), "testB", "B"));
        choiceTableView.addChoiceTableRow(new ChoiceTableItemView(getContext(),
                "Long testLong testLong testLong testLong testLong testLong testLong testLong testLong test" +
                        "Long testLong testLong testLong testLong testLong test" +
                        "Long testLong testLong test", "C"));
    }

}
