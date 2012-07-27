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
import com.ssl.curriculum.math.presenter.MainActivityPresenter;
import com.ssl.curriculum.math.service.GalleryContentProvider;
import com.ssl.curriculum.math.task.FetchGalleryContentTask;

public class MainActivity extends Activity {
	
    private MainActivityPresenter presenter;
    private GalleryThumbnailPage galleryThumbnailPage;
    private GalleryContentProvider galleryContentProvider;
    private VideoPlayer videoPlayer;
    
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

    public void loadActivityById(int id){
    }
    
    private void loadGalleryContent() {
        galleryContentProvider = new GalleryContentProvider(this);
        FetchGalleryContentTask fetchGalleryContentTask = new FetchGalleryContentTask(galleryContentProvider);
        fetchGalleryContentTask.execute();
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        presenter = new MainActivityPresenter(this);
        
        presenter.bindUIElement(MainActivityPresenter.BTN_LEFT, this.findViewById(R.id.main_activity_left_btn));
        presenter.bindUIElement(MainActivityPresenter.BTN_RIGHT, this.findViewById(R.id.main_activity_right_btn));
        presenter.bindUIElement(MainActivityPresenter.BTN_NAVI, this.findViewById(R.id.main_activity_navi_btn));
        presenter.bindUIElement(MainActivityPresenter.FLIPPER, this.findViewById(R.id.main_activity_view_flipper));
        
        presenter.initListeners();
        
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
        galleryThumbnailPage.setGalleryItemClickedListener(new GalleryItemClickedListener() {
            @Override
            public void onGalleryItemClicked(int position) {
                Intent intent = new Intent(MainActivity.this, GalleryFlipperActivity.class);
                startActivity(intent);
            }
        });
    }

}