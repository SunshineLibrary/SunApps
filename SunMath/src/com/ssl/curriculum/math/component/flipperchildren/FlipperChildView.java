package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.widget.RelativeLayout;

public abstract class FlipperChildView extends RelativeLayout{

    public FlipperChildView(Context context) {
        super(context);
    }

    public void onAfterFlippingIn() {

    }

    public void onBeforeFlippingOut() {

    }
}
