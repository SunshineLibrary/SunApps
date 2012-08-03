package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.listener.GalleryItemClickedListener;
import com.ssl.curriculum.math.logic.ActivityFlowController;
import com.ssl.curriculum.math.presenter.MainActivityPresenter;
import com.ssl.curriculum.math.service.GalleryContentProvider;
import com.ssl.curriculum.math.service.mock.MockEdgeContentProvider;
import com.ssl.curriculum.math.task.FetchGalleryContentTask;

public class MainActivity extends Activity {

    private ActivityFlowController flipper;
    private ImageView leftBtn;
    private ImageView rightBtn;
    private ImageView naviBtn;
    private ViewFlipper viewFlipper;

    private MainActivityPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initComponents();
        getDomainActivity(getIntent());
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

    private void getDomainActivity(Intent intent) {
        flipper.loadDomainActivityData(intent.getExtras().getInt("sectionId"), intent.getExtras().getInt("activityId"));
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        leftBtn = (ImageView) this.findViewById(R.id.main_activity_left_btn);
        rightBtn = (ImageView) this.findViewById(R.id.main_activity_right_btn);
        naviBtn = (ImageView) this.findViewById(R.id.main_activity_navi_btn);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.main_activity_view_flipper);
    }

    private void initComponents() {
        presenter = new MainActivityPresenter(this);
        presenter.bindUIElement(MainActivityPresenter.FLIPPER, viewFlipper);
        initListeners();

        flipper = new ActivityFlowController(presenter, new MockEdgeContentProvider(this));
    }


    public GalleryItemClickedListener getGalleryThumbnailItemClickListener() {
        return new GalleryItemClickedListener() {
            @Override
            public void onGalleryItemClicked() {
                Intent intent = new Intent(MainActivity.this, GalleryFlipperActivity.class);
                startActivity(intent);
            }
        };
    }

    public void initListeners() {
        final FlipAnimationManager flipAnimationManager = FlipAnimationManager.getInstance(this);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setInAnimation(flipAnimationManager.getFlipInFromRightAnimation());
                viewFlipper.setOutAnimation(flipAnimationManager.getFlipOutToLeftAnimation());
                presenter.getFlipListener(presenter).onShowPrevious();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setInAnimation(flipAnimationManager.getFlipInFromLeftAnimation());
                viewFlipper.setOutAnimation(flipAnimationManager.getFlipOutToRightAnimation());
                presenter.getFlipListener(presenter).onShowNext();
            }
        });

        naviBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                //Intent intent = new Intent(self.activity, NaviActivity.class);
                //self.activity.startActivity(intent);
            }
        });
    }
}