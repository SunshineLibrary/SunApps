package com.ssl.curriculum.math.adapter;

import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import com.ssl.curriculum.math.component.GalleryPanoramicFlipper;
import com.ssl.curriculum.math.component.GalleryPanoramicItem;
import com.ssl.curriculum.math.data.GalleryContentData;
import com.ssl.curriculum.math.listener.GalleySlideListener;
import com.ssl.curriculum.math.model.GalleryItem;

import java.io.FileNotFoundException;

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
        for (GalleryItem item : galleryContentData.galleryItemsIterator()) {
            try {
                GalleryPanoramicItem galleryPanoramicItem = createGalleryPanoramicItem(item);
                if (galleryPanoramicItem != null) {
                    galleryPanoramicItem.setGallerySlideListener(galleySlideListener);
                    galleryPanoramicFlipper.addView(galleryPanoramicItem);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
        galleryPanoramicFlipper.setDisplayedChild(galleryContentData.getSelectedPosition());
    }

    private GalleryPanoramicItem createGalleryPanoramicItem(GalleryItem item) throws FileNotFoundException {
        GalleryPanoramicItem galleryPanoramicItem = new GalleryPanoramicItem(galleryPanoramicFlipper.getContext());
        ParcelFileDescriptor pfdInput = galleryPanoramicFlipper.getContext().getContentResolver().openFileDescriptor(
                item.getImageUri(), "r");
        if (pfdInput == null) return null;
        galleryPanoramicItem.setItemImageBitmap(BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, null));
        return galleryPanoramicItem;
    }

    public void setGallerySlideListener(GalleySlideListener galleySlideListener) {
        this.galleySlideListener = galleySlideListener;
    }
}
