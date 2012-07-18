package com.ssl.curriculum.math.page;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.adapter.GalleryAdapter;

public class GalleryPage extends LinearLayout {

    private GalleryAdapter galleryAdapter;
    private Gallery gallery;

    public GalleryPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
        initGallery();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.gallery_page, this, false);
        this.addView(viewGroup);
        gallery = (Gallery) findViewById(R.id.gallery_image);
    }

    private void initGallery() {
        galleryAdapter = new GalleryAdapter(getContext());
        gallery.setAdapter(galleryAdapter);
        gallery.setSpacing(5);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
