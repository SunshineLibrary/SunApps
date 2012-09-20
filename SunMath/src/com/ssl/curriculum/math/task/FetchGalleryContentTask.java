package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.data.GalleryContentManager;
import com.ssl.curriculum.math.model.GalleryItem;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.service.GalleryLoader;

import java.util.List;

public class FetchGalleryContentTask extends AsyncTask<Void, Void, List<GalleryItem>> {

    private GalleryLoader galleryLoader;
    private DomainActivityData domainActivity;

    public FetchGalleryContentTask(GalleryLoader galleryLoader, DomainActivityData domainActivity) {
        this.galleryLoader = galleryLoader;
        this.domainActivity = domainActivity;
    }

    @Override
    protected List<GalleryItem> doInBackground(Void... voids) {
        return galleryLoader.loadGalleryContent(domainActivity);
    }

    @Override
    protected void onPostExecute(List<GalleryItem> galleryItems) {
        GalleryContentManager.getInstance().saveGalleryItems(galleryItems);
    }
}
