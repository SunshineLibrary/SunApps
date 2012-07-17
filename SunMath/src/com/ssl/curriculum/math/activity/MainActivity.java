package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {

    private ViewFlipper viewFlipper;
    private ImageButton leftBtn;
    private ImageButton rightBtn;
    private Button naviBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListeners();
    }

    private void initListeners() {
        this.leftBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                viewFlipper.showPrevious();
            }
        });
        this.rightBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
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

    private void initUI() {
        setContentView(R.layout.main_layout);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.main_activity_view_flipper);
        viewFlipper.setInAnimation(MainActivity.this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(MainActivity.this, android.R.anim.fade_out);
        this.leftBtn = (ImageButton) this.findViewById(R.id.main_activity_left_btn);
        this.rightBtn = (ImageButton) this.findViewById(R.id.main_activity_right_btn);
        this.naviBtn = (Button) this.findViewById(R.id.main_activity_navi_btn);
    }
}