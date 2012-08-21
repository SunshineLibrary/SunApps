package com.ssl.curriculum.math.adapter;

import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import com.ssl.curriculum.math.component.GalleryPanoramicFlipper;
import com.ssl.curriculum.math.component.GalleryPanoramicItem;
import com.ssl.curriculum.math.data.GalleryContentData;
import com.ssl.curriculum.math.listener.GalleySlideListener;
import com.ssl.curriculum.math.model.GalleryItem;

import java.io.IOException;
import java.util.Iterator;

public class GalleryPanoramicViewPresenter {

    private GalleryContentData galleryContentData;
    private GalleryPanoramicFlipper galleryPanoramicFlipper;
    private GalleySlideListener galleySlideListener;

    public GalleryPanoramicViewPresenter(GalleryPanoramicFlipper galleryPanoramicFlipper) {
        this.galleryPanoramicFlipper = galleryPanoramicFlipper;
    }

    public void setGalleryData(GalleryContentData galleryContentData) {
        this.galleryContentData = galleryContentData;
        updateView();
    }

    private void updateView() {
        Iterator<GalleryItem> galleryItemIterator = galleryContentData.galleryItemsIterator();
        while (galleryItemIterator.hasNext()) {
            try {
                GalleryItem item = galleryItemIterator.next();
                GalleryPanoramicItem galleryPanoramicItem = createGalleryPanoramicItem(item);
                if (galleryPanoramicItem != null) {
                    galleryPanoramicFlipper.addView(galleryPanoramicItem);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        int selectedPosition = galleryContentData.getSelectedPosition();
        galleryPanoramicFlipper.setDisplayedChild(selectedPosition);
    }

    private GalleryPanoramicItem createGalleryPanoramicItem(GalleryItem item) throws IOException {
        GalleryPanoramicItem galleryPanoramicItem = new GalleryPanoramicItem(galleryPanoramicFlipper.getContext());
        ParcelFileDescriptor pfdInput = galleryPanoramicFlipper.getContext().getContentResolver().openFileDescriptor(item.getImageUri(), "r");
        if (pfdInput == null) return null;
        galleryPanoramicItem.setItemImageBitmap(BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, null));
        galleryPanoramicItem.setGallerySlideListener(galleySlideListener);
        pfdInput.close();
        return galleryPanoramicItem;
    }

    public void setGallerySlideListener(GalleySlideListener galleySlideListener) {
        this.galleySlideListener = galleySlideListener;
    }
}
