package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.OnChoiceChangedListener;

public class ChoiceButton extends ImageView implements View.OnClickListener {

    private enum State {
        ACTIVATE, INACTIVE;
    }

    private State state;

    private OnChoiceChangedListener onChoiceChangedListener;

    public ChoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        deActiveButton();
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (state == State.INACTIVE) {
            activateButton();
        } else {
            deActiveButton();
        }
        if (onChoiceChangedListener != null) {
            onChoiceChangedListener.onChoiceChange();
        }
    }

    private void deActiveButton() {
        state = State.INACTIVE;
        setImageDrawable(getResources().getDrawable(R.drawable.ic_multiple_choice_blank_selection));
    }

    private void activateButton() {
        state = State.ACTIVATE;
        setImageDrawable(getResources().getDrawable(R.drawable.ic_multiple_choice_selected));
    }

    public void setChoiceChangedListener(OnChoiceChangedListener onChoiceChangedListener) {
        this.onChoiceChangedListener = onChoiceChangedListener;
    }
}
