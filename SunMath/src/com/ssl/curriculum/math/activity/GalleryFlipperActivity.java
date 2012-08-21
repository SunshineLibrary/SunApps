package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.os.Bundle;
import com.ssl.curriculum.math.R;

public class GalleryFlipperActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.gallery_flipper);
    }
}
