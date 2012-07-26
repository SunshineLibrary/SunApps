package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.GalleryFloatingPanelListener;
import com.ssl.curriculum.math.task.GalleryFloatingTask;

public class GalleryFloatingPanel extends LinearLayout {
    private GalleryFloatingTask floatingTask;
    private GalleryFloatingPanelListener galleryFloatingPanelListener;
    private ImageView zoomInImageView;
    private ImageView zoomOutImageView;

    public GalleryFloatingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
        initFloatingTask();
        initListener();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup group = (ViewGroup) inflater.inflate(R.layout.gallery_floating_panel, this, false);
        this.addView(group);
        zoomInImageView = (ImageView) findViewById(R.id.gallery_floating_panel_zoom_in);
        zoomOutImageView = (ImageView) findViewById(R.id.gallery_floating_panel_zoom_out);
        setVisibility(View.GONE);
    }

    private void initFloatingTask() {
        floatingTask = new GalleryFloatingTask(GalleryFloatingPanel.this);
    }

    private void initListener() {
        zoomInImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (galleryFloatingPanelListener != null) {
                    galleryFloatingPanelListener.onZoomIn();
                }
            }
        });

        zoomOutImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (galleryFloatingPanelListener != null) {
                    galleryFloatingPanelListener.onZoomOut();
                }
            }
        });
    }

    public void startFloating() {
        if (getVisibility() == VISIBLE) return;
        setVisibility(View.VISIBLE);
        Animation flipInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.gallery_floating_panel_in);
        flipInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingTask.scheduleFlipAway();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.startAnimation(flipInAnimation);
    }

    public void flipAway() {
        post(new Runnable() {
            @Override
            public void run() {
                flipAwayWithAnimation();
            }
        });
    }

    private void flipAwayWithAnimation() {
        Animation flipOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.gallery_floating_panel_out);
        flipOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(flipOutAnimation);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        floatingTask.reset();
        return super.onTouchEvent(event);
    }

    public void setPanelControlListener(GalleryFloatingPanelListener galleryFloatingPanelListener) {
        this.galleryFloatingPanelListener = galleryFloatingPanelListener;
    }
}
