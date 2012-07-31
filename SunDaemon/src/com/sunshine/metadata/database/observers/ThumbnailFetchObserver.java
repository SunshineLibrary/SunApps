package com.sunshine.metadata.database.observers;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
import com.sunshine.metadata.database.tables.ActivityTable;
import com.sunshine.metadata.database.tables.GalleryImageTable;
import com.sunshine.metadata.database.tables.ObservableTable;
import com.sunshine.metadata.provider.Matcher;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.support.api.ApiClient;
import com.sunshine.support.storage.FileDownloadTask;

import java.util.List;
import java.util.Vector;

import static com.sunshine.metadata.provider.MetadataContract.Activities;
import static com.sunshine.metadata.provider.MetadataContract.Downloadable.STATUS;
import static com.sunshine.metadata.provider.MetadataContract.Downloadable._DOWNLOAD_STATUS;
import static com.sunshine.metadata.provider.MetadataContract.GalleryImages;

public class ThumbnailFetchObserver extends TableObserver {

    private UriMatcher sUriMatcher = Matcher.Factory.getMatcher();
    private Context context;

    public ThumbnailFetchObserver(Context context) {
        this.context = context;
    }

    @Override
    public void postInsert(ObservableTable table, Uri uri, ContentValues values, Uri result) {
        if (values.containsKey(BaseColumns._ID)) {
            int id = values.getAsInteger(BaseColumns._ID);
            if (table.getTableName().equals(ActivityTable.TABLE_NAME)) {
                new FileDownloadTask(context,
                        ApiClient.getThumbnailUri("activity", id),
                        Activities.getActivityThumbnailUri(id)).execute();
            } else if (table.getTableName().equals(GalleryImageTable.TABLE_NAME)) {
                new FileDownloadTask(context,
                        ApiClient.getThumbnailUri("gallery_image", id),
                        GalleryImages.getGalleryImageThumbnailUri(id)).execute();
            }
        }
    }
}
