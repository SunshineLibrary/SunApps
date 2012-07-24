package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.model.GalleryItem;
import com.ssl.curriculum.math.page.GalleryThumbnailPage;
import com.ssl.curriculum.math.service.GalleryContentProvider;

import java.util.List;

public class FetchGalleryContentTask extends AsyncTask<Void, Void, List<GalleryItem>> {

    private GalleryContentProvider galleryContentProvider;
    private GalleryThumbnailPage galleryThumbnailPage;

    public FetchGalleryContentTask(GalleryContentProvider galleryContentProvider, GalleryThumbnailPage galleryThumbnailPage) {
        this.galleryContentProvider = galleryContentProvider;
        this.galleryThumbnailPage = galleryThumbnailPage;
    }

    @Override
    protected List<GalleryItem> doInBackground(Void... voids) {
        return galleryContentProvider.loadGalleryContent();
    }

    @Override
    protected void onPostExecute(List<GalleryItem> galleryItems) {
        galleryThumbnailPage.setGalleryData(galleryItems);
    }
}
