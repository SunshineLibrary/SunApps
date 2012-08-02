package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.GalleryItemClickedListener;
import com.ssl.curriculum.math.logic.PageFlipper;
import com.ssl.curriculum.math.presenter.MainActivityPresenter;
import com.ssl.curriculum.math.service.GalleryContentProvider;
import com.ssl.curriculum.math.task.FetchGalleryContentTask;

public class MainActivity extends Activity {

    private MainActivityPresenter presenter;
    private PageFlipper flipper;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadGalleryData();
    }

    private void loadGalleryData() {
        FetchGalleryContentTask task = new FetchGalleryContentTask(new GalleryContentProvider(this));
        task.execute();
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        presenter = new MainActivityPresenter(this);
        flipper = new PageFlipper(presenter);
        flipper.init(1);

        presenter.bindUIElement(MainActivityPresenter.BTN_LEFT, this.findViewById(R.id.main_activity_left_btn));
        presenter.bindUIElement(MainActivityPresenter.BTN_RIGHT, this.findViewById(R.id.main_activity_right_btn));
        presenter.bindUIElement(MainActivityPresenter.BTN_NAVI, this.findViewById(R.id.main_activity_navi_btn));
        presenter.bindUIElement(MainActivityPresenter.FLIPPER, this.findViewById(R.id.main_activity_view_flipper));

        presenter.initListeners();

//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//        RelativeLayout videoFrame = (RelativeLayout) findViewById(R.id.content_screen_video_frame);
//        videoPlayer = (SunLibVideoView) findViewById(R.id.content_screen_video_field);
//        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
//                + R.raw.speaking);
//        videoPlayer.setMediaController(new SunLibMediaController(this));
//        videoPlayer.setVideoURI(video);
//        videoPlayer.setDimensions(655, 437);

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

    public GalleryItemClickedListener getGalleryThumbnailItemClickListener() {
        return new GalleryItemClickedListener() {
            @Override
            public void onGalleryItemClicked() {
                Intent intent = new Intent(MainActivity.this, GalleryFlipperActivity.class);
                startActivity(intent);
            }
        };
    }
}