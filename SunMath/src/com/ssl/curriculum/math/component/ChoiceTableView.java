package com.ssl.curriculum.math.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.OnChoiceChangedListener;
import com.ssl.curriculum.math.model.activity.quiz.QuizChoiceQuestion;

import java.util.List;

public abstract class ChoiceTableView extends LinearLayout implements OnChoiceChangedListener {
    protected TableLayout tableLayout;

    protected ChoiceTableView(Context context) {
        super(context);
        initUI();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.choice_table_view_layout, this, false);
        this.addView(viewGroup);
        tableLayout = (TableLayout) findViewById(R.id.choice_table_layout);
    }

    public void addChoiceTableRow(ChoiceTableItemView itemView) {
        tableLayout.addView(itemView);
        itemView.setOnChoiceChangedListener(this);
    }

    @Override
    public void onChoiceChanged(ChoiceButton choiceButton) {
        selectChoice(choiceButton);
    }

    protected abstract void selectChoice(ChoiceButton choiceButton);

    public abstract void checkAnswer(String answer);

    public void loadChoices(List<QuizChoiceQuestion.Choice> choices) {
        for (QuizChoiceQuestion.Choice choice : choices) {
            addChoiceTableRow(new ChoiceTableItemView(getContext(), choice.choice, choice.body));
        }
    }
}
