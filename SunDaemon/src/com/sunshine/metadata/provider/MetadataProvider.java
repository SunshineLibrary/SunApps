package com.sunshine.metadata.provider;

import android.content.ContentProvider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.database.tables.*;
import com.sunshine.support.storage.SharedStorageProvider;

import java.io.FileNotFoundException;

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
    private static final int GALLERY_IMAGES_ID = 7;
    private static final int ACTIVITIES = 8;

    static {
        sUriMatcher.addURI(AUTHORITY, "packages", PACKAGES);
        sUriMatcher.addURI(AUTHORITY, "courses", COURSES);
        sUriMatcher.addURI(AUTHORITY, "chapters", CHAPTERS);
        sUriMatcher.addURI(AUTHORITY, "lessons", LESSONS);
        sUriMatcher.addURI(AUTHORITY, "sections", SECTIONS);
        sUriMatcher.addURI(AUTHORITY, "activities", ACTIVITIES);
        sUriMatcher.addURI(AUTHORITY, "gallery_images/#", GALLERY_IMAGES_ID);
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
            case GALLERY_IMAGES_ID:
                return dbHandler.getTableManager(GalleryTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
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
            case GALLERY_IMAGES_ID:
                return GALLERY_IMAGES_MIME_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case PACKAGES:
                return dbHandler.getTableManager(PackageTable.TABLE_NAME).insert(
                        uri, values);
            case GALLERY_IMAGES_ID:
                return dbHandler.getTableManager(GalleryTable.TABLE_NAME).insert(
                        uri, values);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case PACKAGES:
                return dbHandler.getTableManager(PackageTable.TABLE_NAME).delete(
                        uri, selection, selectionArgs);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case PACKAGES:
                return dbHandler.getTableManager(PackageTable.TABLE_NAME).update(
                        uri, values, selection, selectionArgs);
            case ACTIVITIES:
                if (values.get(MetadataContract.Downloadable._DOWNLOAD_STATUS).equals(MetadataContract.Downloadable.STATUS.QUEUED.ordinal())) {
                }
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return sharedStorageManager.openFile(uri, mode);
    }

}
