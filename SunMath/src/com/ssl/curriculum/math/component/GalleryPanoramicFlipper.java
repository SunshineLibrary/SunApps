package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.adapter.GalleryPanoramicViewPresenter;
import com.ssl.curriculum.math.data.GalleryContentData;
import com.ssl.curriculum.math.data.GalleryContentManager;
import com.ssl.curriculum.math.listener.GalleryContentFetchedListener;
import com.ssl.curriculum.math.listener.GalleySlideListener;

public class GalleryPanoramicFlipper extends ViewFlipper implements GalleryContentFetchedListener {

    private GalleryPanoramicViewPresenter viewPresenter;

    public GalleryPanoramicFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        fetchGalleryContent();
    }

    private void fetchGalleryContent() {
        viewPresenter = new GalleryPanoramicViewPresenter(this);
        if (GalleryContentManager.getInstance().isDataFetched()) {
            viewPresenter.setGalleryData(GalleryContentManager.getInstance().getGalleryContent());
            return;
        }
        GalleryContentManager.getInstance().registerGalleyContentChangedListener(this);
    }

    @Override
    public void onGalleryContentFetched(GalleryContentData galleryContentData) {
        viewPresenter.setGalleryData(galleryContentData);
        GalleryContentManager.getInstance().removeOnGalleryFetchedListener(this);
    }

    public void setSlideListener(GalleySlideListener galleySlideListener) {
        viewPresenter.setGallerySlideListener(galleySlideListener);
    }
}
