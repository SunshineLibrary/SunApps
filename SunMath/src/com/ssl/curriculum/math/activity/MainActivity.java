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
import com.ssl.curriculum.math.logic.strategy.FetchNextDomainActivityStrategyImpl;
import com.ssl.curriculum.math.presenter.FlipperViewsBuilder;
import com.ssl.curriculum.math.service.GalleryContentProvider;
import com.ssl.curriculum.math.service.mock.MockActivityContentProvider;
import com.ssl.curriculum.math.service.mock.MockEdgeContentProvider;
import com.ssl.curriculum.math.task.FetchActivityTaskManager;
import com.ssl.curriculum.math.task.FetchGalleryContentTask;
public class MainActivity extends Activity {


    private ActivityFlowController flowController;
    private ImageView leftBtn;
    private ImageView rightBtn;
    private ImageView naviBtn;
    private ViewFlipper viewFlipper;

    private FlipperViewsBuilder flipperViewsBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initComponents();
        initListeners();
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
        flowController.loadDomainActivityData(intent.getExtras().getInt("sectionId"), intent.getExtras().getInt("activityId"));
        //flowController.loadDomainActivityData(0, 0);
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        leftBtn = (ImageView) this.findViewById(R.id.main_activity_left_btn);
        rightBtn = (ImageView) this.findViewById(R.id.main_activity_right_btn);
        naviBtn = (ImageView) this.findViewById(R.id.main_activity_navi_btn);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.main_activity_view_flipper);
    }

    private void initComponents() {
        flipperViewsBuilder = new FlipperViewsBuilder(this);
        FetchActivityTaskManager fetchActivityTaskManager = new FetchActivityTaskManager(new MockEdgeContentProvider(this), new MockActivityContentProvider(this));
        flowController = new ActivityFlowController(viewFlipper, flipperViewsBuilder, fetchActivityTaskManager, new FetchNextDomainActivityStrategyImpl(), new FlipAnimationManager(this));
    }


    private void initListeners() {
        leftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                flowController.onShowPrevious();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                flowController.onShowNext();
            }
        });

        naviBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        flipperViewsBuilder.setGalleryThumbnailItemClickListener(new GalleryItemClickedListener() {
            @Override
            public void onGalleryItemClicked() {
                Intent intent = new Intent(MainActivity.this, GalleryFlipperActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flowController.destroyFlipperSubViews();
    }
}