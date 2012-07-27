package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.component.videoview.VideoPlayer;
import com.ssl.curriculum.math.listener.GalleryItemClickedListener;
import com.ssl.curriculum.math.page.GalleryThumbnailPage;
import com.ssl.curriculum.math.service.GalleryContentProvider;
import com.ssl.curriculum.math.task.FetchGalleryContentTask;

public class MainActivity extends Activity {

    private ViewFlipper viewFlipper;
    private ImageView leftBtn;
    private ImageView rightBtn;
    private ImageView naviBtn;
    private GalleryThumbnailPage galleryThumbnailPage;
    private GalleryContentProvider galleryContentProvider;
    private VideoPlayer videoPlayer;
    private FlipAnimationManager flipAnimationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListeners();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        loadGalleryContent();
    }

    private void loadGalleryContent() {
        galleryContentProvider = new GalleryContentProvider(this);
        FetchGalleryContentTask fetchGalleryContentTask = new FetchGalleryContentTask(galleryContentProvider);
        fetchGalleryContentTask.execute();
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.main_activity_view_flipper);
        this.leftBtn = (ImageView) this.findViewById(R.id.main_activity_left_btn);
        this.rightBtn = (ImageView) this.findViewById(R.id.main_activity_right_btn);
        this.naviBtn = (ImageView) this.findViewById(R.id.main_activity_navi_btn);
        galleryThumbnailPage = (GalleryThumbnailPage) findViewById(R.id.gallery_thumbnail_page);

        videoPlayer = (VideoPlayer) findViewById(R.id.content_screen_video_field);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.speaking);
        videoPlayer.setVideoURI(video);
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
        flipAnimationManager = FlipAnimationManager.getInstance(this);
        this.leftBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setInAnimation(flipAnimationManager.getFlipInFromRightAnimation());
                viewFlipper.setOutAnimation(flipAnimationManager.getFlipOutToLeftAnimation());
                viewFlipper.showPrevious();
            }
        });
        this.rightBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                viewFlipper.setInAnimation(flipAnimationManager.getFlipInFromLeftAnimation());
                viewFlipper.setOutAnimation(flipAnimationManager.getFlipOutToRightAnimation());
                viewFlipper.showNext();


            }
        });
        this.naviBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NaviActivity.class);
                startActivity(intent);
            }
        });
        galleryThumbnailPage.setGalleryItemClickedListener(new GalleryItemClickedListener() {
            @Override
            public void onGalleryItemClicked(int position) {
                Intent intent = new Intent(MainActivity.this, GalleryFlipperActivity.class);
                startActivity(intent);
            }
        });
    }

}