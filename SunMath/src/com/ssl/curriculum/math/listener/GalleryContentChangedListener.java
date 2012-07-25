package com.ssl.curriculum.math.listener;

import com.ssl.curriculum.math.model.GalleryItem;

import java.util.List;

public interface GalleryContentChangedListener {

    void onContentChanged(List<GalleryItem> galleryItems);
}
