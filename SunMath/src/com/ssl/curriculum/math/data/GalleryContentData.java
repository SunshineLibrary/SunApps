package com.ssl.curriculum.math.data;

import com.ssl.curriculum.math.listener.GalleryContentChangedListener;
import com.ssl.curriculum.math.model.GalleryItem;

import java.util.ArrayList;
import java.util.List;

public class GalleryContentData {
    private static GalleryContentData data;
    private List<GalleryItem> galleryItems;
    private List<GalleryContentChangedListener> galleryContentChangedListeners;

    private GalleryContentData() {
        galleryItems = new ArrayList<GalleryItem>();
        galleryContentChangedListeners = new ArrayList<GalleryContentChangedListener>();
    }

    public static GalleryContentData getInstance() {
        if (data == null) {
            data = new GalleryContentData();
        }
        return data;
    }

    public void saveGalleryItems(List<GalleryItem> galleryItems) {
        this.galleryItems = galleryItems;
        notifyGalleryContentChanged();
    }

    private void notifyGalleryContentChanged() {
        for (GalleryContentChangedListener galleryContentChangedListener : galleryContentChangedListeners) {
            galleryContentChangedListener.onContentChanged(galleryItems);
        }
    }

    public void registerGalleyContentChangedListener(GalleryContentChangedListener galleryContentChangedListener) {
        galleryContentChangedListeners.add(galleryContentChangedListener);
    }
}
