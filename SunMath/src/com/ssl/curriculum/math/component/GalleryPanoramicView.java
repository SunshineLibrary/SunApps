package com.ssl.curriculum.math.component;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.listener.GalleySlideListener;

public class GalleryPanoramicView extends RelativeLayout implements GalleySlideListener {
    private static final int FLIPPING_DISTANCE = 120;
    private GalleryPanoramicFlipper panoramicViewerFlipper;
    private FlipAnimationManager flipAnimationManager;
    private GalleryFloatingPanel galleryFloatingPanel;

    public GalleryPanoramicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
//        initFlipView();
        initListener();
    }

    private void initListener() {
        panoramicViewerFlipper.setSlideListener(this);
    }

    private void initFlipView() {
        GalleryPanoramicItem item01 = new GalleryPanoramicItem(getContext());
        item01.setGallerySlideListener(this);
        item01.setItemImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.image02));
        GalleryPanoramicItem item02 = new GalleryPanoramicItem(getContext());
        item02.setGallerySlideListener(this);

        item02.setItemImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.image03));

        panoramicViewerFlipper.addView(item01, 0);
        panoramicViewerFlipper.addView(item02, 1);
        panoramicViewerFlipper.setDisplayedChild(0);
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.gallery_panoramic_page, this, false);
        addView(viewGroup);
        panoramicViewerFlipper = (GalleryPanoramicFlipper) findViewById(R.id.gallery_panoramic_flipper);
        galleryFloatingPanel = (GalleryFloatingPanel) findViewById(R.id.gallery_panoramic_panel);
    }

    private boolean isShowPrevious(float xDistance) {
        return xDistance < -FLIPPING_DISTANCE;
    }

    private boolean isShowNext(float xDistance) {
        return xDistance > FLIPPING_DISTANCE;
    }

    @Override
    public void onSlide(float slideDistance) {
        flipAnimationManager = FlipAnimationManager.getInstance(getContext());
        if (isShowNext(slideDistance)) {
            panoramicViewerFlipper.setInAnimation(flipAnimationManager.getFlipInFromRightAnimation());
            panoramicViewerFlipper.setOutAnimation(flipAnimationManager.getFlipOutToLeftAnimation());
            panoramicViewerFlipper.showNext();
            return;
        }
        if (isShowPrevious(slideDistance)) {
            panoramicViewerFlipper.setInAnimation(flipAnimationManager.getFlipInFromLeftAnimation());
            panoramicViewerFlipper.setOutAnimation(flipAnimationManager.getFlipOutToRightAnimation());
            panoramicViewerFlipper.showPrevious();
            return;
        }
    }
}
