package com.ssl.curriculum.math.data;

import com.ssl.curriculum.math.listener.GalleryContentFetchedListener;
import com.ssl.curriculum.math.model.GalleryItem;

import java.util.ArrayList;
import java.util.List;

public class GalleryContentManager {

    private static GalleryContentManager manager;
    private GalleryContentData galleryContentData;
    private List<GalleryContentFetchedListener> galleryContentFetchedListeners;

    private GalleryContentManager() {
        galleryContentFetchedListeners = new ArrayList<GalleryContentFetchedListener>();
        galleryContentData = new GalleryContentData();
    }

    public static GalleryContentManager getInstance() {
        if (manager == null) {
            manager = new GalleryContentManager();
        }
        return manager;
    }

    public void saveGalleryItems(List<GalleryItem> galleryItems) {
        galleryContentData.saveGalleryItems(galleryItems);
        notifyGalleryContentFetched();
    }

    private void notifyGalleryContentFetched() {
        for (GalleryContentFetchedListener galleryContentFetchedListener : galleryContentFetchedListeners) {
            if (galleryContentFetchedListener == null) {
                galleryContentFetchedListeners.remove(galleryContentFetchedListener);
                continue;
            }
            galleryContentFetchedListener.onGalleryContentFetched(galleryContentData);
        }
    }

    public boolean isDataFetched() {
        return galleryContentData.getCount() != 0;
    }

    public GalleryContentData getGalleryContent() {
        return galleryContentData;
    }

    public void registerGalleyContentChangedListener(GalleryContentFetchedListener galleryContentFetchedListener) {
        galleryContentFetchedListeners.add(galleryContentFetchedListener);
    }

    public void removeOnGalleryFetchedListener(GalleryContentFetchedListener galleryContentFetchedListener) {
        galleryContentFetchedListeners.remove(galleryContentFetchedListener);
    }
}
