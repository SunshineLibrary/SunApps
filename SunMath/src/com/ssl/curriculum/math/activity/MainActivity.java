package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.videoview.SunLibMediaController;
import com.ssl.curriculum.math.component.videoview.SunLibVideoView;
import com.ssl.curriculum.math.page.GalleryThumbnailPage;
import com.ssl.curriculum.math.service.GalleryContentProvider;
import com.ssl.curriculum.math.task.FetchGalleryContentTask;

public class MainActivity extends Activity {

    private ViewFlipper viewFlipper;
    private ImageView leftBtn;
    private ImageView rightBtn;
    private ImageView naviBtn;
    private Animation animFlipInFromRight;
    private Animation animFlipInFromLeft;
    private Animation animFlipOutToRight;
    private Animation animFlipOutToLeft;
    private GalleryThumbnailPage galleryThumbnailPage;
    private GalleryContentProvider galleryContentProvider;
    private SunLibVideoView videoPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListeners();
        loadAnimation();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadGalleryContent();
    }

    private void loadGalleryContent() {
        galleryContentProvider = new GalleryContentProvider(this);
        FetchGalleryContentTask fetchGalleryContentTask = new FetchGalleryContentTask(galleryContentProvider, galleryThumbnailPage);
        fetchGalleryContentTask.execute();
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.main_activity_view_flipper);
        this.leftBtn = (ImageView) this.findViewById(R.id.main_activity_left_btn);
        this.rightBtn = (ImageView) this.findViewById(R.id.main_activity_right_btn);
        this.naviBtn = (ImageView) this.findViewById(R.id.main_activity_navi_btn);
        galleryThumbnailPage = (GalleryThumbnailPage) findViewById(R.id.gallery_thumbnail_page);
        
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        RelativeLayout videoFrame = (RelativeLayout) findViewById(R.id.content_screen_video_frame);
        videoPlayer = (SunLibVideoView) findViewById(R.id.content_screen_video_field);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.speaking);
        videoPlayer.setMediaController(new SunLibMediaController(this));
        videoPlayer.setVideoURI(video);
        videoPlayer.setDimensions(655, 437);

//        videoPlayer.start();

    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//            videoPlayer.setDimensions(displayHeight, displayWidth);
//            videoPlayer.getHolder().setFixedSize(displayHeight, displayWidth);
//
//        } else {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//
//            videoPlayer.setDimensions(displayWidth, smallHeight);
//            videoPlayer.getHolder().setFixedSize(displayWidth, smallHeight);
//        }
//    }

    private void initListeners() {
        this.leftBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setInAnimation(animFlipInFromRight);
                viewFlipper.setOutAnimation(animFlipOutToLeft);
                viewFlipper.showPrevious();
            }
        });
        this.rightBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setInAnimation(animFlipInFromLeft);
                viewFlipper.setOutAnimation(animFlipOutToRight);
                viewFlipper.showNext();


            }
        });
        this.naviBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NaviActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadAnimation() {
        animFlipInFromLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_in_from_left);
        animFlipInFromRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_in_from_right);
        animFlipOutToRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_out_to_right);
        animFlipOutToLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_out_to_left);
    }
}