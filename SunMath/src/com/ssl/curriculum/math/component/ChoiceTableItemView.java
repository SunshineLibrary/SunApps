package com.ssl.curriculum.math.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.OnChoiceChangedListener;

public class ChoiceTableItemView extends TableRow {

    private ImageView answerImageView;
    private ChoiceButton answerSelectedBtn;
    private TextView quizContentWebView;
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
        quizContentWebView = (TextView) findViewById(R.id.choice_table_item_webview);
        questionTagView = (TextView) findViewById(R.id.choice_table_item_tag);

        quizContentWebView.setText(question);
        questionTagView.setText(token);
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
        return questionTagView.getText().toString();
    }

    public void setOnChoiceChangedListener(OnChoiceChangedListener onChoiceChangedListener) {
        answerSelectedBtn.setOnChoiceChangedListener(onChoiceChangedListener);
    }
}
