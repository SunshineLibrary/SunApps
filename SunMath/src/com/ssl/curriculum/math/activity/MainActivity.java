package com.ssl.curriculum.math.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.listener.GalleryItemClickedListener;
import com.ssl.curriculum.math.logic.ActivityFlowController;
import com.ssl.curriculum.math.logic.strategy.FetchNextDomainActivityStrategyImpl;
import com.ssl.curriculum.math.presenter.FlipperViewsBuilder;
import com.ssl.curriculum.math.service.ActivityContentProvider;
import com.ssl.curriculum.math.service.SectionActivityContentProvider;
import com.ssl.curriculum.math.task.FetchActivityTaskManager;
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
    }

    private void getDomainActivity(Intent intent) {
        int sectionId = intent.getExtras().getInt("sectionId");
        int activityId = intent.getExtras().getInt("activityId");
        Log.i("open activity", "sectionId=" + sectionId + "," + "activityId" + activityId);
        flowController.loadDomainActivityData(sectionId, activityId);
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
        FetchActivityTaskManager fetchActivityTaskManager = new FetchActivityTaskManager(new ActivityContentProvider(this), new SectionActivityContentProvider(this));
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