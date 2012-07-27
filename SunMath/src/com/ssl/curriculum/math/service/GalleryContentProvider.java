package com.ssl.curriculum.math.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import com.ssl.curriculum.math.model.GalleryItem;
import com.sunshine.metadata.provider.MetadataContract;

import java.util.ArrayList;
import java.util.List;

public class GalleryContentProvider {
    private Context context;

    public GalleryContentProvider(Context context) {
        this.context = context;
    }

    public List<GalleryItem> loadGalleryContent() {
        List<GalleryItem> list = new ArrayList<GalleryItem>();
        ContentResolver contentResolver = context.getContentResolver();
        String[] columns = {MetadataContract.Gallery._ID, MetadataContract.Gallery._IMAGE_PATH, MetadataContract.Gallery._THUMBNAIL_PATH, MetadataContract.Gallery._DESCRIPTION};
        Cursor cursor = contentResolver.query(MetadataContract.Gallery.CONTENT_URI, columns, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int imageIndex = cursor.getColumnIndex(MetadataContract.Gallery._IMAGE_PATH);
                int thumbnailIndex = cursor.getColumnIndex(MetadataContract.Gallery._THUMBNAIL_PATH);
                int descriptionIndex = cursor.getColumnIndex(MetadataContract.Gallery._DESCRIPTION);
                GalleryItem item = new GalleryItem();
                item.setImageUri(cursor.getString(imageIndex));
                item.setThumbnailUri(cursor.getString(thumbnailIndex));
                item.setDescription(cursor.getString(descriptionIndex));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
