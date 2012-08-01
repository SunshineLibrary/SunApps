package com.ssl.curriculum.math.data;

import com.ssl.curriculum.math.model.GalleryItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GalleryContentData {
    private List<GalleryItem> galleryItems;
    private int selectedPosition;

    public GalleryContentData() {
        galleryItems = new ArrayList<GalleryItem>();
    }

    public void saveGalleryItems(List<GalleryItem> galleryItems) {
        this.galleryItems = galleryItems;
    }

    public int getCount() {
        return galleryItems.size();
    }

    public GalleryItem getGalleryItem(int position) {
        return galleryItems.get(position);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public Iterator<GalleryItem> galleryItemsIterator() {
        return galleryItems.iterator();
    }
}
