package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.ChoiceTableItemView;
import com.ssl.curriculum.math.component.ChoiceTableView;
import com.ssl.curriculum.math.listener.MultiChoiceJSInterface;

public class QuizMultiChoiceView extends QuizQuestionView {

    private ChoiceTableView choiceTableView;

    public QuizMultiChoiceView(Context context, int questionId) {
        super(context, questionId);
    }

    @Override
    protected void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_multichoice_flip_layout, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_multichoice_flipper_child_question);
        choiceTableView = (ChoiceTableView) findViewById(R.id.quiz_multichoice_table);
    }

    @Override
    protected void initWebView() {
        super.initWebView();
        questionWebView.addJavascriptInterface(new MultiChoiceJSInterface(), "multiChoice");
    }

    @Override
    protected void loadQuizHtml(String quizContent) {
        questionWebView.loadUrl("file:///android_asset/multichoice.html");
        choiceTableView.addChoiceTableRow(new ChoiceTableItemView(getContext()));
        choiceTableView.addChoiceTableRow(new ChoiceTableItemView(getContext()));
        choiceTableView.addChoiceTableRow(new ChoiceTableItemView(getContext()));
    }
}
