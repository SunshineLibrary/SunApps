package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.os.Bundle;
import com.ssl.curriculum.math.R;

public class NaviActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.navigation_layout);
    }

}
