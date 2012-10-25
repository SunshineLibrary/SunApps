package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.webkit.WebView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.OnChoiceChangedListener;
import com.ssl.curriculum.math.utils.QuizHtmlLoader;

public class ChoiceTableItemView extends TableRow {
    private static final String CHOICE_ITEM_DOT = ".";
    private ImageView answerImageView;
    private ChoiceButton answerSelectedBtn;
    private WebView quizContentWebView;
    private TextView questionTagView;

    public ChoiceTableItemView(Context context, String question, String token) {
        super(context);
        initUI(question, token);
    }

    private void initUI(String question, String token) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.choice_table_item_view, this, false);
        this.addView(viewGroup);
        answerImageView = (ImageView) findViewById(R.id.choice_table_item_answer_result_indicator);
        answerSelectedBtn = (ChoiceButton) findViewById(R.id.choice_table_item_btn);
        quizContentWebView = (WebView) findViewById(R.id.choice_table_item_webview);
        questionTagView = (TextView) findViewById(R.id.choice_table_item_tag);
        questionTagView.setText(token + CHOICE_ITEM_DOT);
        loadWebView(question);
    }

    private void loadWebView(String question) {
        quizContentWebView.getSettings().setJavaScriptEnabled(true);
        quizContentWebView.getSettings().setAllowFileAccess(true);
        quizContentWebView.getSettings().setDomStorageEnabled(true);
        quizContentWebView.setScrollBarStyle(0);

        final String data = QuizHtmlLoader.getInstance(getContext()).loadQuestionChoiceWithNewContent(question);
        // same usage as in QuizQuestionView
        quizContentWebView.loadDataWithBaseURL("http://test", data, "text/html", "utf-8", null);
    }

    @Override
    public boolean isSelected() {
        return answerSelectedBtn.isSelected();
    }

    @Override
    public void setSelected(boolean selected) {
        answerSelectedBtn.setSelected(false);
    }

    public String getToken() {
        String tagString = questionTagView.getText().toString();
        return tagString.substring(0, tagString.lastIndexOf("."));
    }

    public void setOnChoiceChangedListener(OnChoiceChangedListener onChoiceChangedListener) {
        answerSelectedBtn.setOnChoiceChangedListener(onChoiceChangedListener);
    }

    public void showCorrect() {
        answerImageView.setVisibility(View.VISIBLE);
        answerImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_choice_correct));
    }

    public void showInCorrect() {
        answerImageView.setVisibility(View.VISIBLE);
        answerImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_choice_incorrect));
    }
}
