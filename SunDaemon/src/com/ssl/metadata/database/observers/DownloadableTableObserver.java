package com.ssl.metadata.database.observers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import com.ssl.metadata.provider.Matcher;
import com.ssl.metadata.provider.MetadataContract;

import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.downloader.DownloadTaskParams;
import com.ssl.support.downloader.tasks.DownloadTask;
import com.ssl.support.services.DownloadService;

import java.util.LinkedList;
import java.util.List;

import static com.ssl.metadata.provider.MetadataContract.Downloadable._DOWNLOAD_STATUS;

public class DownloadableTableObserver extends TableObserver {

    private UriMatcher sUriMatcher = Matcher.Factory.getMatcher();
    private Context context;

    public DownloadableTableObserver(Context context) {
        this.context = context;
    }

    @Override
    public void postUpdate(Uri uri, ContentValues values, String selection, String[] selectionArgs, int result) {
        if (result > 0 && values.containsKey(_DOWNLOAD_STATUS)
                && values.getAsInteger(_DOWNLOAD_STATUS) == MetadataContract.Downloadable.STATUS_QUEUED) {
            switch(sUriMatcher.match(uri)) {
                case Matcher.ACTIVITIES:
                case Matcher.GALLERY_IMAGES:
                case Matcher.BOOKS:
                case Matcher.SECTIONS:
                    downloadBatch(uri, selection, selectionArgs);
                    break;
                case Matcher.ACTIVITIES_ID:
                case Matcher.GALLERY_IMAGES_ID:
                case Matcher.BOOKS_ID:
                case Matcher.SECTIONS_ID:
                    downloadContent(uri);
                    break;
                default:
            }
        }
    }


    private void downloadBatch(Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = context.getContentResolver().query(uri, new String[]{BaseColumns._ID},
                selection, selectionArgs, null);

        List<Uri> downloadUris = new LinkedList<Uri>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                downloadUris.add(uri.buildUpon().appendPath("" + id).build());
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (Uri u: downloadUris) {
            downloadContent(u);
        }
    }

    private void downloadContent(Uri uri) {
        switch(sUriMatcher.match(uri)) {
            case Matcher.ACTIVITIES_ID:
                downloadActivity(uri);
                break;
            case Matcher.GALLERY_IMAGES_ID:
                downloadGalleryImage(uri);
                break;
            case Matcher.BOOKS_ID:
                downloadBook(uri);
                break;
            case Matcher.SECTIONS_ID:
                downloadSection(uri);
            default:
        }
    }

    private void downloadSection(Uri uri) {
        int section_id = Integer.parseInt(uri.getLastPathSegment());
        enqueueDownloadTask(DownloadTask.TYPE_SECTION, section_id);
    }


    private void downloadActivity(Uri uri) {
        int activity_id= Integer.parseInt(uri.getLastPathSegment());
        enqueueDownloadTask(DownloadTask.TYPE_ACTIVITY, activity_id);
    }

    private void downloadBook(Uri uri) {
        throw new UnsupportedOperationException();
    }

    public void downloadGalleryImage(Uri uri) {
        throw new UnsupportedOperationException();
    }

    private void enqueueDownloadTask(int type, int id) {
        DownloadTaskParams params = new DownloadTaskParams(type, id);
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.PARAMS_KEY, params);
        context.startService(intent);
    }
}
