package com.ssl.metadata.database.observers;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.BaseColumns;
import com.ssl.metadata.database.views.SectionActivitiesView;
import com.ssl.support.downloader.DownloadTaskParams;
import com.ssl.support.downloader.MonitoredFileDownloadTask;
import com.ssl.metadata.provider.Matcher;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.services.DownloadService;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import static com.ssl.metadata.provider.MetadataContract.Activities;
import static com.ssl.metadata.provider.MetadataContract.Downloadable._DOWNLOAD_STATUS;
import static com.ssl.metadata.provider.MetadataContract.GalleryImages;

public class DownloadableTableObserver extends TableObserver {

    private UriMatcher sUriMatcher = Matcher.Factory.getMatcher();
    private Context context;
    private ApiClient apiClient;

    public DownloadableTableObserver(Context context) {
        this.context = context;
        this.apiClient = ApiClientFactory.newApiClient(context);
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
        Cursor cursor = context.getContentResolver().query(
                MetadataContract.Sections.getSectionActivitiesUri(section_id),
                new String[]{Activities._ID, Activities._TYPE}, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(Activities._ID));
                int type = cursor.getInt(cursor.getColumnIndex(Activities._TYPE));
                downloadActivity(Activities.getActivityUri(id), id, type, uri);
            } while (cursor.moveToNext());
        }
    }


    private void downloadActivity(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, new String[]{Activities._TYPE}, null, null, null);
        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(uri.getLastPathSegment());
            int type = cursor.getInt(cursor.getColumnIndex(Activities._TYPE));
            downloadActivity(uri, id, type, null);
        }
        cursor.close();
    }

    private void downloadActivity(Uri uri, int id, int type, Uri notifyUri) {
        switch (type) {
            case Activities.TYPE_VIDEO:
                enqueueDownloadTask(
                        apiClient.getDownloadUri("activities", id), Activities.getActivityVideoUri(id), uri, notifyUri);
                break;
            case Activities.TYPE_TEXT:
                enqueueDownloadTask(
                        apiClient.getDownloadUri("activities", id), Activities.getActivityTextUri(id), uri, notifyUri);
                break;
            case Activities.TYPE_HTML:
                enqueueDownloadTask(
                        apiClient.getDownloadUri("activities", id), Activities.getActivityHtmlUri(id), uri, notifyUri);
                break;
            case Activities.TYPE_GALLERY:
                ContentValues values = new ContentValues();
                values.put(Activities._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_QUEUED);
                context.getContentResolver().update(GalleryImages.CONTENT_URI, values,
                        GalleryImages._GALLERY_ID + "=?", new String[]{String.valueOf(id)});
                break;
            case Activities.TYPE_QUIZ:
                enqueueDownloadTask(null, null, uri, notifyUri);
                break;
            default:
        }

    }

    private void downloadBook(Uri uri) {
        int id = Integer.parseInt(uri.getLastPathSegment());
        new MonitoredFileDownloadTask(context, apiClient.getDownloadUri("books", id), uri, uri).execute();
    }

    public void downloadGalleryImage(Uri uri) {
        int id = Integer.parseInt(uri.getLastPathSegment());
        new MonitoredFileDownloadTask(context,
                apiClient.getDownloadUri("images", id),
                GalleryImages.getGalleryImageUri(id), uri).execute();
    }

    private void enqueueDownloadTask(Uri remoteUri, Uri localUri, Uri updateUri, Uri notifyUri) {
        DownloadTaskParams params = new DownloadTaskParams(remoteUri, localUri, updateUri, notifyUri);
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.PARAMS_KEY, params);
        context.startService(intent);
    }
}
