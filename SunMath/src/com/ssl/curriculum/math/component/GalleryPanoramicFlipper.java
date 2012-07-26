package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.listener.GalleryFloatingPanelListener;

public class GalleryPanoramicFlipper extends ViewFlipper implements GalleryFloatingPanelListener {

    public GalleryPanoramicFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onZoomIn() {
        ((GalleryPanoramicItem) getChildAt(getDisplayedChild())).zoomIn();
    }

    @Override
    public void onZoomOut() {
        ((GalleryPanoramicItem) getChildAt(getDisplayedChild())).zoomOut();
    }
}
