package com.ssl.curriculum.math.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.ssl.curriculum.math.R;

public class GalleryPanoramicItem extends FrameLayout {
    private ImageView itemImageView;

    private static final float MAX_SCALE = 2.0f;
    private static final float MIN_SCALE = 0.3f;
    private static float STEP = 0.05f;
    private float scale = 1.0f;
    private float originalWidth, originalHeight;

    public GalleryPanoramicItem(Context context) {
        super(context);
        initUI();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.gallery_panoramic_item, this, false);
        this.addView(viewGroup);
        itemImageView = (ImageView) findViewById(R.id.gallery_panoramic_item_image);
    }

    public void setItemImageBitmap(Bitmap bitmap) {
        originalWidth = bitmap.getWidth();
        originalHeight = bitmap.getHeight();
        itemImageView.setImageBitmap(bitmap);
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
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        itemImageView.setImageMatrix(matrix);
        itemImageView.setScaleType(ImageView.ScaleType.MATRIX);
        itemImageView.invalidate();
    }
}
