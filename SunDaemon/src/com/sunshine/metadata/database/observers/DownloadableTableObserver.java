package com.sunshine.metadata.database.observers;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import com.sunshine.metadata.provider.Matcher;
import com.sunshine.support.api.ApiClient;
import com.sunshine.support.downloader.FileDownloadTask;

import java.util.List;
import java.util.Vector;

import static com.sunshine.metadata.provider.MetadataContract.Activities;
import static com.sunshine.metadata.provider.MetadataContract.Downloadable.STATUS;
import static com.sunshine.metadata.provider.MetadataContract.Downloadable._DOWNLOAD_STATUS;
import static com.sunshine.metadata.provider.MetadataContract.GalleryImages;

public class DownloadableTableObserver extends TableObserver {

    private UriMatcher sUriMatcher = Matcher.Factory.getMatcher();
    private Context context;

    public DownloadableTableObserver(Context context) {
        this.context = context;
    }

    @Override
    public void postUpdate(Uri uri, ContentValues values, String selection, String[] selectionArgs, int result) {
        if (result > 0 && values.containsKey(_DOWNLOAD_STATUS)
                && values.getAsInteger(_DOWNLOAD_STATUS) == STATUS.QUEUED.ordinal()) {
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
                    new FileDownloadTask(context,
                            ApiClient.getDownloadUri("activities", id),
                            Activities.getActivityVideoUri(id)).execute();
                    break;
                case Activities.TYPE_GALLERY:
                    ContentValues values = new ContentValues();
                    values.put(Activities._DOWNLOAD_STATUS, STATUS.QUEUED.ordinal());
                    context.getContentResolver().update(GalleryImages.CONTENT_URI, values,
                            "gallery_id = ?", new String[]{String.valueOf(id)});
                    break;
                default:
            }
        }
        cursor.close();
    }

    public void downloadGalleryImage(Uri uri) {
        int id = Integer.parseInt(uri.getLastPathSegment());
        new FileDownloadTask(context,
                ApiClient.getDownloadUri("images", id),
                GalleryImages.getGalleryImageUri(id)).execute();
    }
}
