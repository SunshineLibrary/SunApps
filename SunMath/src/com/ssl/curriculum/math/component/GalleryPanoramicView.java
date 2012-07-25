package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.ssl.curriculum.math.R;

public class GalleryPanoramicView extends RelativeLayout{

    private ImageViewerFlipper imageViewerFlipper;

    public GalleryPanoramicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
        initListener();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.gallery_panoramic_page, this, false);
        addView(viewGroup);
        imageViewerFlipper = (ImageViewerFlipper) findViewById(R.id.gallery_panoramic_flipper);
    }

    private void initListener() {
    }
}
