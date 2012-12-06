package com.ssl.curriculum.math.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.activity.VideoActivityView;
import com.ssl.curriculum.math.component.videoview.VideoPlayer;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;
import com.ssl.curriculum.math.data.SectionActivitiesLoader;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;

public class MainActivity extends Activity {

    private ImageView leftBtn;
    private ImageView rightBtn;
    private ImageView naviBtn;
    private ActivityViewer mActivityViewer;
    private SectionActivitiesLoader mActivitiesLoader;

    private int sectionId;
    private int initActivityId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initComponents();
        initListeners();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadActivity();
    }

    private void loadActivity() {
        Log.i("open activities", "sectionId=" + sectionId + "," + "activityId" + initActivityId);
        LinkedActivityData data = mActivitiesLoader.getLoadedActivity(initActivityId);
        mActivityViewer.startActivity(data);
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        leftBtn = (ImageView) this.findViewById(R.id.main_activity_left_btn);
        rightBtn = (ImageView) this.findViewById(R.id.main_activity_right_btn);
        naviBtn = (ImageView) this.findViewById(R.id.main_activity_navi_btn);
        mActivityViewer = (ActivityViewer) this.findViewById(R.id.main_activity_view_flipper);
        mActivityViewer.setNextBtn(rightBtn);
    }

    private void initComponents() {
        Intent intent = getIntent();
        sectionId = intent.getExtras().getInt("sectionId");
        initActivityId = intent.getExtras().getInt("activityId");
        mActivitiesLoader = new SectionActivitiesLoader(this, sectionId);
        mActivityViewer.setSectionId(sectionId);
        mActivityViewer.setActivityFinishListener(new ActivityViewer.ActivityFinishListener() {
            @Override
            public void onActivityFinish() {
                finish();
            }
        });
    }

    private void initListeners() {
        leftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mActivityViewer.onPrevBtnClicked(v);
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mActivityViewer.onNextBtnClicked(v);
            }
        });

        naviBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityViewer.destroy();
    }
    
    
    /*@Override
     * Suggest this method ,because it's the method leading to the target 
    protected void onUserLeaveHint() {
    	// TODO Auto-generated method stub
    	super.onUserLeaveHint();
    }*/
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	mActivityViewer.pause();
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	this.registerReceiver(new BroadcastReceiver(){

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				System.out.println("收到广播");
				mActivityViewer.onNextBtnClicked(rightBtn);
			}}, new IntentFilter("com.liucong.next"));
    }
}