package com.ssl.curriculum.math.model;

import android.net.Uri;
import com.ssl.metadata.provider.MetadataContract;

public class GalleryItem {
    private Uri imageUri;
    private Uri thumbnailUri;
    private String description;

    public GalleryItem(int galleryId, String description) {
        imageUri = MetadataContract.GalleryImages.getGalleryImageUri(galleryId);
        thumbnailUri = MetadataContract.GalleryImages.getGalleryImageThumbnailUri(galleryId);
        this.description = description;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    public String getDescription() {
        return description;
    }
}
