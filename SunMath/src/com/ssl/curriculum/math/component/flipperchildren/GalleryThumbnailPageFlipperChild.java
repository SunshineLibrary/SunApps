package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.adapter.GalleryGridAdapter;
import com.ssl.curriculum.math.data.GalleryContentData;
import com.ssl.curriculum.math.data.GalleryContentManager;
import com.ssl.curriculum.math.listener.GalleryContentFetchedListener;
import com.ssl.curriculum.math.listener.GalleryItemClickedListener;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.service.GalleryContentProvider;
import com.ssl.curriculum.math.task.FetchGalleryContentTask;

public class GalleryThumbnailPageFlipperChild extends FlipperChildView implements GalleryContentFetchedListener {
    private GridView gridview;
    private GalleryGridAdapter gridAdapter;
    private TextView title;
    private GalleryItemClickedListener galleryItemClickedListener;
    private DomainActivityData domainActivity;

    public GalleryThumbnailPageFlipperChild(Context context, DomainActivityData domainActivity) {
        super(context);
        this.domainActivity = domainActivity;
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
        gridAdapter = new GalleryGridAdapter(getContext());
        gridview.setAdapter(gridAdapter);
        fetchGalleryContent();
    }

    private void fetchGalleryContent() {
        GalleryContentManager.getInstance().registerGalleyContentChangedListener(this);
        FetchGalleryContentTask fetchGalleryContentTask = new FetchGalleryContentTask(new GalleryContentProvider(getContext()), domainActivity);
        fetchGalleryContentTask.execute();
    }

    private void initListener() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridAdapter.setSelectedPosition(position);
                if (galleryItemClickedListener != null) {
                    galleryItemClickedListener.onGalleryItemClicked();
                }
            }
        });
    }

    @Override
    public void onGalleryContentFetched(GalleryContentData galleryContentData) {
        gridAdapter.setGalleryData(galleryContentData);
        GalleryContentManager.getInstance().removeOnGalleryFetchedListener(this);
    }

    public void setGalleryItemClickedListener(GalleryItemClickedListener galleryItemClickedListener) {
        this.galleryItemClickedListener = galleryItemClickedListener;
    }
}
