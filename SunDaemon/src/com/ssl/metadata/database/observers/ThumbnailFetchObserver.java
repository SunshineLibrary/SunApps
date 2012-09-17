package com.ssl.metadata.database.observers;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;
import com.ssl.metadata.database.tables.*;
import com.ssl.metadata.provider.Matcher;
import com.ssl.support.api.ApiClient;
import com.ssl.support.downloader.FileDownloadTask;
import com.ssl.metadata.provider.MetadataContract.BookCollections;
import com.ssl.metadata.provider.MetadataContract.Books;
import com.ssl.support.api.ApiClientFactory;

import static com.ssl.metadata.provider.MetadataContract.Activities;
import static com.ssl.metadata.provider.MetadataContract.GalleryImages;

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
