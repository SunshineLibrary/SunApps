package com.ssl.curriculum.math.page;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.adapter.GalleryAdapter;
import com.ssl.curriculum.math.model.GalleryItem;

import java.util.List;

public class GalleryThumbnailPage extends LinearLayout {
    private GridView gridview;
    private GalleryAdapter adapter;

    public GalleryThumbnailPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
        initListener();
        initAdapter();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.gallery_page, this, false);
        this.addView(viewGroup);
        gridview = (GridView) findViewById(R.id.image_viewer_grid_view);
    }

    private void initAdapter() {
        adapter = new GalleryAdapter(getContext());
        gridview.setAdapter(adapter);
    }

    private void initListener() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    public void setGalleryData(List<GalleryItem> galleryItemList) {
        adapter.setGalleryData(galleryItemList);
    }
}
