package com.ssl.metadata.database.observers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;
import com.ssl.metadata.database.tables.*;
import com.ssl.metadata.provider.Matcher;
import com.ssl.support.api.ApiClient;
import com.ssl.support.downloader.DownloadTaskParams;
import com.ssl.support.downloader.tasks.AsyncFileDownloadTask;
import com.ssl.metadata.provider.MetadataContract.BookCollections;
import com.ssl.metadata.provider.MetadataContract.Books;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.downloader.tasks.DownloadTask;
import com.ssl.support.services.DownloadService;

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
            } else if (table.getTableName().equals(GalleryImageTable.TABLE_NAME)) {
            } else if (table.getTableName().equals(BookTable.TABLE_NAME)) {
                enqueueDownloadTask(DownloadTask.TYPE_BOOK_THUMB, id);
            } else if (table.getTableName().equals(BookCollectionTable.TABLE_NAME)) {
                enqueueDownloadTask(DownloadTask.TYPE_BOOK_COLLECTION_THUMB, id);
            }
        }
    }

    private void enqueueDownloadTask(int type, int id) {
        DownloadTaskParams params = new DownloadTaskParams(type, id);
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.PARAMS_KEY, params);
        context.startService(intent);
    }
}
