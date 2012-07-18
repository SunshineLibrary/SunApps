package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.R;

public class MainActivity extends Activity {

    private ViewFlipper viewFlipper;
    private ImageView leftBtn;
    private ImageView rightBtn;
    private ImageView naviBtn;
    private Animation animFlipInFromRight;
    private Animation animFlipInFromLeft;
    private Animation animFlipOutToRight;
    private Animation animFlipOutToLeft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListeners();
        loadAnimation();
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.main_activity_view_flipper);
        this.leftBtn = (ImageView) this.findViewById(R.id.main_activity_left_btn);
        this.rightBtn = (ImageView) this.findViewById(R.id.main_activity_right_btn);
        this.naviBtn = (ImageView) this.findViewById(R.id.main_activity_navi_btn);
    }

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