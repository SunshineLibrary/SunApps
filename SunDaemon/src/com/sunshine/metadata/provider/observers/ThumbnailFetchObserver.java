package com.sunshine.metadata.provider.observers;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;
import com.sunshine.metadata.database.tables.*;
import com.sunshine.metadata.provider.Matcher;
import com.sunshine.metadata.provider.MetadataContract.BookCollections;
import com.sunshine.metadata.provider.MetadataContract.Books;
import com.sunshine.support.api.ApiClient;
import com.sunshine.support.api.ApiClientFactory;
import com.sunshine.support.downloader.FileDownloadTask;

import static com.sunshine.metadata.provider.MetadataContract.Activities;
import static com.sunshine.metadata.provider.MetadataContract.GalleryImages;

public class ThumbnailFetchObserver extends TableObserver {

    private UriMatcher sUriMatcher = Matcher.Factory.getMatcher();
    private Context context;
    private ApiClient apiClient;

    public ThumbnailFetchObserver(Context context) {
        this.context = context;
        this.apiClient = ApiClientFactory.newApiClient(context);
    }

    @Override
    public void postInsert(ObservableTable table, Uri uri, ContentValues values, Uri result) {
        if (values.containsKey(BaseColumns._ID)) {
            int id = values.getAsInteger(BaseColumns._ID);
            if (table.getTableName().equals(ActivityTable.TABLE_NAME)) {
                new FileDownloadTask(context,
                        apiClient.getThumbnailUri("activities", id),
                        Activities.getActivityThumbnailUri(id)).execute();
            } else if (table.getTableName().equals(GalleryImageTable.TABLE_NAME)) {
                new FileDownloadTask(context,
                        apiClient.getThumbnailUri("images", id),
                        GalleryImages.getGalleryImageThumbnailUri(id)).execute();
            } else if (table.getTableName().equals(BookTable.TABLE_NAME)) {
                new FileDownloadTask(context,
                        apiClient.getThumbnailUri("books", id),
                        Books.getBookThumbnailUri(id)).execute();
            } else if (table.getTableName().equals(BookCollectionTable.TABLE_NAME)) {
                new FileDownloadTask(context,
                        apiClient.getThumbnailUri("book_collections", id),
                        BookCollections.getBookCollectionThumbnailUri(id)).execute();
            }
        }
    }
}
