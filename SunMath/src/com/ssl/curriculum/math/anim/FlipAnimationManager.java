package com.ssl.curriculum.math.anim;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.ssl.curriculum.math.R;

public class FlipAnimationManager {

    private Animation animFlipInFromRight;
    private Animation animFlipInFromLeft;
    private Animation animFlipOutToRight;
    private Animation animFlipOutToLeft;

    public FlipAnimationManager(Context context) {
        animFlipInFromLeft = AnimationUtils.loadAnimation(context, R.anim.flip_in_from_left);
        animFlipInFromRight = AnimationUtils.loadAnimation(context, R.anim.flip_in_from_right);
        animFlipOutToRight = AnimationUtils.loadAnimation(context, R.anim.flip_out_to_right);
        animFlipOutToLeft = AnimationUtils.loadAnimation(context, R.anim.flip_out_to_left);
    }

    public Animation getFlipInFromLeftAnimation() {
        return animFlipInFromLeft;
    }

    public Animation getFlipOutToRightAnimation() {
        return animFlipOutToRight;
    }

    public Animation getFlipInFromRightAnimation() {
        return animFlipInFromRight;
    }

    public Animation getFlipOutToLeftAnimation() {
        return animFlipOutToLeft;
    }
}
