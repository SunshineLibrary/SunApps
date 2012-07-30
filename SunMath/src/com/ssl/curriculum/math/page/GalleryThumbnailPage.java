package com.ssl.curriculum.math.page;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.adapter.GalleryAdapter;
import com.ssl.curriculum.math.data.GalleryContentData;
import com.ssl.curriculum.math.listener.GalleryContentChangedListener;
import com.ssl.curriculum.math.listener.GalleryItemClickedListener;
import com.ssl.curriculum.math.model.GalleryItem;

import java.util.List;

public class GalleryThumbnailPage extends LinearLayout implements GalleryContentChangedListener {
    private GridView gridview;
    private GalleryAdapter adapter;
    private TextView title;
    private GalleryItemClickedListener galleryItemClickedListener;

    public GalleryThumbnailPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
        initListener();
        initAdapter();
    }

    public GalleryThumbnailPage(Context context) {
        super(context);
        initUI();
        initListener();
        initAdapter();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.gallery_page, this, false);
        this.addView(viewGroup);
        gridview = (GridView) findViewById(R.id.image_viewer_grid_view);
        title = (TextView) findViewById(R.id.gallery_page_title);
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
        GalleryContentData.getInstance().registerGalleyContentChangedListener(this);
        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (galleryItemClickedListener != null) {
                    galleryItemClickedListener.onGalleryItemClicked(1);
                }
            }
        });
    }

    @Override
    public void onContentChanged(List<GalleryItem> galleryItems) {
        adapter.setGalleryData(galleryItems);
    }


    public void setGalleryItemClickedListener(GalleryItemClickedListener galleryItemClickedListener) {
        this.galleryItemClickedListener = galleryItemClickedListener;
    }
}
