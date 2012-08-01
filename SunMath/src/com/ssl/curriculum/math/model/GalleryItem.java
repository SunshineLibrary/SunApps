package com.ssl.curriculum.math.model;

public class GalleryItem {
    private String imageUri;
    private String thumbnailUri;
    private String description;
    private int galleryID;

    public String getImageUri() {
        return imageUri;
    }

    public String getThumbnailUri() {
        return thumbnailUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGalleryID(int galleryID) {
        this.galleryID = galleryID;
    }
}
