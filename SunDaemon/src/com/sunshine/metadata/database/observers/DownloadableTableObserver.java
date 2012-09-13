package com.sunshine.metadata.database.observers;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.BaseColumns;
import com.sunshine.metadata.provider.Matcher;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.support.api.ApiClient;
import com.sunshine.support.api.ApiClientFactory;
import com.sunshine.support.downloader.MonitoredFileDownloadTask;
import com.sunshine.support.services.DownloadService;

import java.util.List;
import java.util.Vector;

import static com.sunshine.metadata.provider.MetadataContract.Activities;
import static com.sunshine.metadata.provider.MetadataContract.Downloadable._DOWNLOAD_STATUS;
import static com.sunshine.metadata.provider.MetadataContract.GalleryImages;

public class DownloadableTableObserver extends TableObserver {

    private UriMatcher sUriMatcher = Matcher.Factory.getMatcher();
    private Context context;
    private ApiClient apiClient;
    private DownloadService downloadService;
    private ServiceConnection mConnection;

    public DownloadableTableObserver(Context context) {
        this.context = context;
        this.apiClient = ApiClientFactory.newApiClient(context);

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                downloadService = ((DownloadService.DownloadBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                downloadService = null;
            }
        };
    }

    @Override
    public void postUpdate(Uri uri, ContentValues values, String selection, String[] selectionArgs, int result) {
        if (result > 0 && values.containsKey(_DOWNLOAD_STATUS)
                && values.getAsInteger(_DOWNLOAD_STATUS) == MetadataContract.Downloadable.STATUS_QUEUED) {
            switch(sUriMatcher.match(uri)) {
                case Matcher.ACTIVITIES:
                case Matcher.GALLERY_IMAGES:
                case Matcher.BOOKS:
                    downloadContents(uri, selection, selectionArgs);
                    break;
                case Matcher.ACTIVITIES_ID:
                case Matcher.GALLERY_IMAGES_ID:
                case Matcher.BOOKS_ID:
                    downloadContent(uri);
                    break;
                default:
            }
        }
    }

    private void downloadContents(Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = context.getContentResolver().query(uri, new String[]{BaseColumns._ID},
                selection, selectionArgs, null);
        List<Uri> downloadUris= new Vector<Uri>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                downloadUris.add(uri.buildUpon().appendPath(String.valueOf(id)).build());
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (Uri u: downloadUris){
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
            default:
        }
    }


    public void downloadActivity(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, new String[]{Activities._TYPE}, null, null, null);
        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(uri.getLastPathSegment());
            int type = cursor.getInt(cursor.getColumnIndex(Activities._TYPE));
            switch (type) {
                case Activities.TYPE_VIDEO:
                    startSerialDownload(new MonitoredFileDownloadTask(context,
                            apiClient.getDownloadUri("activities", id),
                            Activities.getActivityVideoUri(id), uri));
                    break;
                case Activities.TYPE_TEXT:
                    startSerialDownload(new MonitoredFileDownloadTask(context,
                            apiClient.getDownloadUri("activities", id),
                            Activities.getActivityTextUri(id), uri));
                    break;
                case Activities.TYPE_GALLERY:
                    ContentValues values = new ContentValues();
                    values.put(Activities._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_QUEUED);
                    context.getContentResolver().update(GalleryImages.CONTENT_URI, values,
                            GalleryImages._GALLERY_ID + "=?", new String[]{String.valueOf(id)});
                    break;
                case Activities.TYPE_QUIZ:
                    values = new ContentValues();
                    values.put(Activities._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_DOWNLOADED);
                    context.getContentResolver().update(uri, values, null, null);
                    context.getContentResolver().notifyChange(uri, null);
                    break;
                default:
            }
        }
        cursor.close();
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

    private void startSerialDownload(MonitoredFileDownloadTask task) {
        getDownloadService().addDownloadTask(task);
    }

    private DownloadService getDownloadService() {
        if (downloadService == null) {
            Intent intent = new Intent(context, DownloadService.class);
            context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
        return downloadService;
    }
}
