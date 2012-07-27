package com.sunshine.metadata.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.database.tables.*;
import com.sunshine.support.api.ApiClient;
import com.sunshine.support.storage.FileDownloadTask;
import com.sunshine.support.storage.SharedStorageProvider;

import java.io.FileNotFoundException;

import static com.sunshine.metadata.provider.MetadataContract.Activities;
import static com.sunshine.metadata.provider.MetadataContract.Activities.*;
import static com.sunshine.metadata.provider.MetadataContract.Downloadable;

public class MetadataProvider extends ContentProvider {

    /*
      * Defining constants for matching the content URI
      */
    private static final String AUTHORITY = MetadataContract.AUTHORITY;

    private static final UriMatcher sUriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    private static final int PACKAGES = 1;
    private static final int COURSES = 3;
    private static final int CHAPTERS = 4;
    private static final int LESSONS = 5;
    private static final int SECTIONS = 6;
    private static final int GALLERY_IMAGES = 7;
    private static final int ACTIVITIES = 8;
    private static final int ACTIVITIES_ID = 9;

    static {
        sUriMatcher.addURI(AUTHORITY, "packages", PACKAGES);
        sUriMatcher.addURI(AUTHORITY, "courses", COURSES);
        sUriMatcher.addURI(AUTHORITY, "chapters", CHAPTERS);
        sUriMatcher.addURI(AUTHORITY, "lessons", LESSONS);
        sUriMatcher.addURI(AUTHORITY, "sections", SECTIONS);
        sUriMatcher.addURI(AUTHORITY, "activities", ACTIVITIES);
        sUriMatcher.addURI(AUTHORITY, "activities/#", ACTIVITIES_ID);
        sUriMatcher.addURI(AUTHORITY, "activities/gallery/images/#", GALLERY_IMAGES);
    }

    /*
      * Constants for handling MIME_TYPE
      */
    public static final String DIR_MIME_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY;
    public static final String ITEM_MIME_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY;

    private static final String METADATA_MIME_TYPE = DIR_MIME_TYPE
            + ".metadata";

    private static final String METADATA_ID_MIME_TYPE = ITEM_MIME_TYPE
            + ".metadata";

    private static final String GALLERY_IMAGES_MIME_TYPE = MetadataProvider.DIR_MIME_TYPE
            + ".images";


    private MetadataDBHandler dbHandler;
    private SharedStorageProvider sharedStorageManager;

    @Override
    public boolean onCreate() {
        dbHandler = new MetadataDBHandler(getContext());
        sharedStorageManager = new SharedStorageProvider(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case PACKAGES:
                return dbHandler.getTableManager(PackageTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case COURSES:
                return dbHandler.getTableManager(CourseTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case CHAPTERS:
                return dbHandler.getTableManager(ChapterTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case LESSONS:
                return dbHandler.getTableManager(LessonTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case SECTIONS:
                return dbHandler.getTableManager(SectionTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case GALLERY_IMAGES:
                return dbHandler.getTableManager(GalleryTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case ACTIVITIES:
                return dbHandler.getTableManager(ActivityTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case ACTIVITIES_ID:
                return dbHandler.getTableManager(ActivityTable.TABLE_NAME).query(
                        uri, projection, BaseColumns._ID + "=?",
                        new String[]{uri.getLastPathSegment()}, sortOrder);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case PACKAGES:
                return METADATA_MIME_TYPE;
            case COURSES:
                return METADATA_MIME_TYPE;
            case CHAPTERS:
                return METADATA_MIME_TYPE;
            case LESSONS:
                return METADATA_MIME_TYPE;
            case SECTIONS:
                return METADATA_MIME_TYPE;
            case GALLERY_IMAGES:
                return GALLERY_IMAGES_MIME_TYPE;
            default:
                return sharedStorageManager.getType(uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case GALLERY_IMAGES:
                return dbHandler.getTableManager(GalleryTable.TABLE_NAME).insert(
                        uri, values);
            case ACTIVITIES:
                return dbHandler.getTableManager(ActivityTable.TABLE_NAME).insert(
                        uri, values);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case ACTIVITIES_ID:
                return updateActivity(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException();
        }
    }

    private int updateActivity(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Table table = dbHandler.getTableManager(ActivityTable.TABLE_NAME);
        Integer status;
        if ((status = values.getAsInteger(Downloadable._DOWNLOAD_STATUS)) != null) {
            if (status == STATUS.QUEUED.ordinal()) {
                Cursor cursor = table.query(uri, new String[] {Activities._TYPE}, null, null, null);
                if (cursor.moveToFirst()) {
                    int id = Integer.parseInt(uri.getLastPathSegment());
                    switch (cursor.getInt(cursor.getColumnIndex(Activities._TYPE))) {
                        case TYPE_GALLERY:
                            new FileDownloadTask(getContext(),
                                    ApiClient.getDownloadUri("gallery_activity", id),
                                    Activities.getActivityGalleryUri(id)).execute();
                            break;
                        case TYPE_VIDEO:
                            new FileDownloadTask(getContext(),
                                    ApiClient.getDownloadUri("video_activity", id),
                                    Activities.getActivityVideoUri(id)).execute();
                            break;
                        default:
                    }
                }
            }
        }
        return table.update(uri, values, selection, selectionArgs);
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return sharedStorageManager.openFile(uri, mode);
    }

}
