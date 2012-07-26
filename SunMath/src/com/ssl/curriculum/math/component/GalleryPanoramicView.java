package com.ssl.curriculum.math.component;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.anim.FlipAnimationManager;

public class GalleryPanoramicView extends RelativeLayout implements GestureDetector.OnGestureListener {
    private static final int FLIPPING_DISTANCE = 120;
    private GalleryPanoramicFlipper panoramicViewerFlipper;
    private GestureDetector gestureDetector;
    private FlipAnimationManager flipAnimationManager;

    public GalleryPanoramicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
        initListener();
        initFlipData();
    }

    private void initFlipData() {
        GalleryPanoramicItem item01 = new GalleryPanoramicItem(getContext());
        item01.setItemImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.image02));
        GalleryPanoramicItem item02 = new GalleryPanoramicItem(getContext());
        item02.setItemImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.image04));

        panoramicViewerFlipper.addView(item01, 0);
        panoramicViewerFlipper.addView(item02, 1);
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.gallery_panoramic_page, this, false);
        addView(viewGroup);
        panoramicViewerFlipper = (GalleryPanoramicFlipper) findViewById(R.id.gallery_panoramic_flipper);
    }

    private void initListener() {
        gestureDetector = new GestureDetector(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        flipAnimationManager = FlipAnimationManager.getInstance(getContext());
        if (isShowNext(e1, e2)) {
            panoramicViewerFlipper.setInAnimation(flipAnimationManager.getFlipInFromRightAnimation());
            panoramicViewerFlipper.setOutAnimation(flipAnimationManager.getFlipOutToLeftAnimation());
            panoramicViewerFlipper.showNext();
            return true;
        }
        if (isShowPrevious(e1, e2)) {
            panoramicViewerFlipper.setInAnimation(flipAnimationManager.getFlipInFromLeftAnimation());
            panoramicViewerFlipper.setOutAnimation(flipAnimationManager.getFlipOutToRightAnimation());
            panoramicViewerFlipper.showPrevious();
            return true;
        }
        return true;
    }

    private boolean isShowPrevious(MotionEvent e1, MotionEvent e2) {
        return e1.getX() - e2.getX() < -FLIPPING_DISTANCE;
    }

    private boolean isShowNext(MotionEvent e1, MotionEvent e2) {
        return e1.getX() - e2.getX() > FLIPPING_DISTANCE;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent event, MotionEvent event1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }
}
