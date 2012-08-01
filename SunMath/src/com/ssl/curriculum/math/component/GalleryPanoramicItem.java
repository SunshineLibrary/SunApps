package com.ssl.curriculum.math.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.GalleySlideListener;

public class GalleryPanoramicItem extends FrameLayout {
    private static final float MAX_SCALE = 2.0f;
    private static final float MIN_SCALE = 0.3f;
    private static float STEP = 0.05f;
    private float scale = 1.0f;
    private FrameLayout container;
    private TouchableImageView touchableImageView;

    public GalleryPanoramicItem(Context context) {
        super(context);
        initUI();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.gallery_panoramic_item, this, false);
        this.addView(viewGroup);
        container = (FrameLayout) findViewById(R.id.gallery_panoramic_item_container);
        touchableImageView = (TouchableImageView) findViewById(R.id.gallery_panoramic_item_image);
    }

    public void setItemImageBitmap(Bitmap bitmap) {
        touchableImageView.setImageBitmap(bitmap);
    }

    public void zoomIn() {
        scale += STEP;
        if (scale > MAX_SCALE) scale = MAX_SCALE;
        redrawImage();
    }

    public void zoomOut() {
        scale -= STEP;
        if (scale < MIN_SCALE) scale = MIN_SCALE;
        redrawImage();
    }

    private void redrawImage() {

    }

    public void setGallerySlideListener(GalleySlideListener galleySlideListener) {
        touchableImageView.setGallerySlideListener(galleySlideListener);
    }
}
