package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.OnChoiceChangedListener;

public class ChoiceButton extends ImageView implements View.OnClickListener {

    private OnChoiceChangedListener onChoiceChangedListener;
    private boolean isActivated;

    public ChoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        deActiveButton();
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (onChoiceChangedListener != null) {
            onChoiceChangedListener.onChoiceChanged(this);
        }
    }

    private void deActiveButton() {
        isActivated = false;
        setImageDrawable(getResources().getDrawable(R.drawable.ic_choice_blank_selection));
    }


    private void activateButton() {
        isActivated = true;
        setImageDrawable(getResources().getDrawable(R.drawable.ic_choice_selected));
    }

    @Override
    public boolean isSelected() {
        return isActivated;
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            activateButton();
            return;
        }
        deActiveButton();
    }

    // only used by multiple answer multiple choice
    public void toggle(MultipleAnswersChoiceTableView v) {
        if (isSelected()) {
            deActiveButton();
            v.decreaseCounter();
        } else {
            activateButton();
            v.increaseCounter();
        }
    }

    public void setOnChoiceChangedListener(OnChoiceChangedListener onChoiceChangedListener) {
        this.onChoiceChangedListener = onChoiceChangedListener;
    }
}

