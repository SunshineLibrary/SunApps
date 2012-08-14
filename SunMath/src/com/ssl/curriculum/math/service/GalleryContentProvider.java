package com.ssl.curriculum.math.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import com.ssl.curriculum.math.model.GalleryItem;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.sunshine.metadata.provider.MetadataContract;

import java.util.ArrayList;
import java.util.List;

public class GalleryContentProvider {
    private Context context;

    public GalleryContentProvider(Context context) {
        this.context = context;
    }

    public List<GalleryItem> loadGalleryContent(DomainActivityData domainActivity) {
        List<GalleryItem> list = new ArrayList<GalleryItem>();
        ContentResolver contentResolver = context.getContentResolver();
        String[] columns = {MetadataContract.GalleryImages._ID, MetadataContract.GalleryImages._GALLERY_ID, MetadataContract.GalleryImages._INTRO};
        Cursor cursor = contentResolver.query(MetadataContract.GalleryImages.CONTENT_URI, columns,
                MetadataContract.GalleryImages._GALLERY_ID + "=?", new String[]{String.valueOf(domainActivity.activityId)}, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(MetadataContract.GalleryImages._ID);
                int descriptionIndex = cursor.getColumnIndex(MetadataContract.GalleryImages._INTRO);
                GalleryItem item = new GalleryItem(cursor.getInt(idIndex), cursor.getString(descriptionIndex));
                list.add(item);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
}
