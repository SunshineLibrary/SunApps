package com.sunshine.metadata.provider;

import android.content.ContentProvider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.database.tables.*;
import com.sunshine.support.storage.FileStorage;
import com.sunshine.support.storage.FileStorageManager;
import com.sunshine.utils.FileLog;

import java.io.File;
import java.io.FileNotFoundException;

public class MetadataProvider extends ContentProvider {

    /*
      * Defining constants for matching the content URI
      */
    private static final String AUTHORITY = MetadataContract.AUTHORITY;

    private static final UriMatcher sUriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    private static final int PACKAGES = 1;
    private static final int PACKAGES_ID = 2;
    private static final int COURSES = 3;
    private static final int CHAPTERS = 4;
    private static final int LESSONS = 5;
    private static final int GALLERY = 6;
    private static final int GALLERY_IMAGE = 7;
    private static final int GALLERY_THUMBNAIL = 8;

    static {
        sUriMatcher.addURI(AUTHORITY, "packages", PACKAGES);
        sUriMatcher.addURI(AUTHORITY, "packages/#", PACKAGES_ID);
        sUriMatcher.addURI(AUTHORITY, "courses", COURSES);
        sUriMatcher.addURI(AUTHORITY, "chapters", CHAPTERS);
        sUriMatcher.addURI(AUTHORITY, "lessons", LESSONS);
        sUriMatcher.addURI(AUTHORITY, "gallery", GALLERY);
        sUriMatcher.addURI(AUTHORITY, "gallery/image/*", GALLERY_IMAGE);
        sUriMatcher.addURI(AUTHORITY, "gallery/thumbnail/*", GALLERY_THUMBNAIL);
    }

    /*
      * Constants for handling MIME_TYPE
      */
    private static final String DIR_MIME_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY;
    private static final String ITEM_MIME_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY;

    private static final String METADATA_MIME_TYPE = DIR_MIME_TYPE
            + ".metadata";

    private static final String METADATA_ID_MIME_TYPE = ITEM_MIME_TYPE
            + ".metadata";

    private static final String GALLERY_IMAGE_MIME_TYPE = DIR_MIME_TYPE
            + ".image";

    private static final String GALLERY_THUMBNAIL_MIME_TYPE = DIR_MIME_TYPE
            + ".thumbnail";


    private MetadataDBHandler dbHandler;
    private FileLog log;
    private FileStorage fileStorage;

    @Override
    public boolean onCreate() {
        dbHandler = new MetadataDBHandler(getContext());
        log = FileLog.setupLogFile();
        fileStorage = FileStorageManager.getInstance().getReadableFileStorage();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case PACKAGES:
                return dbHandler.getTableManager(PackageTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case PACKAGES_ID:
                return dbHandler.getTableManager(PackageTable.TABLE_NAME).query(
                        uri, projection, MetadataContract.Packages._ID + " = ?",
                        new String[]{uri.getLastPathSegment()}, sortOrder);
            case COURSES:
                return dbHandler.getTableManager(CourseTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case CHAPTERS:
                return dbHandler.getTableManager(ChapterTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case LESSONS:
                return dbHandler.getTableManager(LessonTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case GALLERY:
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
            case PACKAGES_ID:
                return METADATA_ID_MIME_TYPE;
            case COURSES:
                return METADATA_MIME_TYPE;
            case CHAPTERS:
                return METADATA_MIME_TYPE;
            case LESSONS:
                return METADATA_MIME_TYPE;
            case GALLERY_IMAGE:
                return GALLERY_IMAGE_MIME_TYPE;
            case GALLERY_THUMBNAIL:
                return GALLERY_THUMBNAIL_MIME_TYPE;
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
            case GALLERY:
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
            case PACKAGES_ID:
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
            case PACKAGES_ID:
                return dbHandler.getTableManager(PackageTable.TABLE_NAME).update(
                        uri, values, MetadataContract.Packages._ID + "=?",
                        new String[]{uri.getLastPathSegment()});
            default:
                throw new IllegalArgumentException();
        }
    }


    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        log.log(getContext(), "openFile Uri:" + uri.toString());
        switch (sUriMatcher.match(uri)) {
            case GALLERY_IMAGE:
            case GALLERY_THUMBNAIL:
                return readImageFile(uri);
            default:
                return null;
        }
    }

    private ParcelFileDescriptor readImageFile(Uri uri) {
        String path = uri.getPath();
        log.log(getContext(), "uri path=" + uri.getPath());
        String imagePath = trimPreviousSlash(path);
        File imageFile = fileStorage.readFile(imagePath);
        if (imageFile.exists()) {
            ParcelFileDescriptor open = null;
            try {
                open = ParcelFileDescriptor.open(imageFile, ParcelFileDescriptor.MODE_READ_ONLY);
            } catch (Exception e) {
                log.log(getContext(), "open parce file error.");
            }
            return open;
        }
        log.log(getContext(), "can not file, path=" + path);
        return null;
    }

    private String trimPreviousSlash(String path) {
        String thumbnailPath = null;
        if (path.startsWith(File.separator)) {
            thumbnailPath = path.substring(path.indexOf(File.separator));
        }
        return thumbnailPath;
    }
}
