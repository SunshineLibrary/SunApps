package com.ssl.curriculum.math.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import com.ssl.curriculum.math.R;

public class ChoiceTableItemView extends TableRow {

    private ImageView answerImageView;
    private ChoiceButton answerSelectedBtn;
    private TextView quizContentWebView;

    public ChoiceTableItemView(Context context) {
        super(context);
        initUI();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.choice_table_item_view, this, false);
        this.addView(viewGroup);
        answerImageView = (ImageView) findViewById(R.id.choice_table_item_answer_result_indicator);
        answerSelectedBtn = (ChoiceButton) findViewById(R.id.choice_table_item_btn);
        quizContentWebView = (TextView) findViewById(R.id.choice_table_item_webview);
    }

}
