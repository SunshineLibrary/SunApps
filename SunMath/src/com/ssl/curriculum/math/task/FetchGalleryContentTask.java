package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.data.GalleryContentManager;
import com.ssl.curriculum.math.model.GalleryItem;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.service.GalleryContentProvider;

import java.util.List;

public class FetchGalleryContentTask extends AsyncTask<Void, Void, List<GalleryItem>> {

    private GalleryContentProvider galleryContentProvider;
    private DomainActivityData domainActivity;

    public FetchGalleryContentTask(GalleryContentProvider galleryContentProvider, DomainActivityData domainActivity) {
        this.galleryContentProvider = galleryContentProvider;
        this.domainActivity = domainActivity;
    }

    @Override
    protected List<GalleryItem> doInBackground(Void... voids) {
        return galleryContentProvider.loadGalleryContent(domainActivity);
    }

    @Override
    protected void onPostExecute(List<GalleryItem> galleryItems) {
        GalleryContentManager.getInstance().saveGalleryItems(galleryItems);
    }
}
