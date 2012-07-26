package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.ssl.curriculum.math.R;

public class GalleryFloatingPanel extends LinearLayout{

    public GalleryFloatingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup group = (ViewGroup) inflater.inflate(R.layout.gallery_floating_panel, this, false);
        this.addView(group);
    }
}
