package com.ssl.curriculum.math.component;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * @author Bowen Sun
 * @version 1.0
 */

public abstract class FlipperChildView extends RelativeLayout {

    public FlipperChildView(Context context) {
        super(context);
    }

    public void onAfterFlippingIn() {

    }

    public void onBeforeFlippingOut() {

    }

    public void onDestroy() {
    }
}
