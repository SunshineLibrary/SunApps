package com.sunshine.metadata.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.database.MetadataDBHandlerFactory;
import com.sunshine.metadata.database.tables.*;
import com.sunshine.support.storage.SharedStorageManager;

import java.io.FileNotFoundException;

public class MetadataProvider extends ContentProvider {

    private static final String AUTHORITY = MetadataContract.AUTHORITY;

    private static final UriMatcher sUriMatcher = Matcher.Factory.getMatcher();

    private MetadataDBHandler dbHandler;

    private SharedStorageManager sharedStorageManager;

    @Override
    public boolean onCreate() {
        dbHandler = MetadataDBHandlerFactory.newMetadataDBHandler(getContext());
        sharedStorageManager = new SharedStorageManager(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case Matcher.PACKAGES:
                return dbHandler.getTableManager(PackageTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.COURSES:
                return dbHandler.getTableManager(CourseTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.CHAPTERS:
                return dbHandler.getTableManager(ChapterTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.LESSONS:
                return dbHandler.getTableManager(LessonTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.SECTIONS:
                return dbHandler.getTableManager(SectionTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.GALLERY_IMAGES:
                return dbHandler.getTableManager(GalleryImageTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.ACTIVITIES:
                return dbHandler.getTableManager(ActivityTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.ACTIVITIES_ID:
                return dbHandler.getTableManager(ActivityTable.TABLE_NAME).query(
                        uri, projection, BaseColumns._ID + "=?",
                        new String[]{uri.getLastPathSegment()}, sortOrder);
            case Matcher.BOOKS:
                return dbHandler.getTableManager(BookTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.BOOK_COLLECTIONS:
                return dbHandler.getTableManager(BookCollectionTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.BOOK_LISTS:
                return dbHandler.getTableManager(BookListTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.PROBLEMS:
                return dbHandler.getTableManager(ProblemTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);

            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case Matcher.PACKAGES:
                return MimeType.METADATA_MIME_TYPE;
            case Matcher.COURSES:
                return MimeType.METADATA_MIME_TYPE;
            case Matcher.CHAPTERS:
                return MimeType.METADATA_MIME_TYPE;
            case Matcher.LESSONS:
                return MimeType.METADATA_MIME_TYPE;
            case Matcher.SECTIONS:
                return MimeType.METADATA_MIME_TYPE;
            case Matcher.GALLERY_IMAGES:
                return MimeType.GALLERY_IMAGES_MIME_TYPE;
            case Matcher.PROBLEMS:
                            return MimeType.METADATA_MIME_TYPE;
            default:
                return sharedStorageManager.getType(uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case Matcher.GALLERY_IMAGES:
                return dbHandler.getTableManager(GalleryImageTable.TABLE_NAME).insert(
                        uri, values);
            case Matcher.ACTIVITIES:
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
            case Matcher.ACTIVITIES_ID:
                selection = MetadataContract.Activities._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
            case Matcher.ACTIVITIES:
                return dbHandler.getTableManager(ActivityTable.TABLE_NAME).update(
                        uri, values, selection, selectionArgs);
            case Matcher.GALLERY_IMAGES_ID:
                selection = MetadataContract.GalleryImages._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
            case Matcher.GALLERY_IMAGES:
                return dbHandler.getTableManager(GalleryImageTable.TABLE_NAME).update(
                        uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return sharedStorageManager.openFile(uri, mode);
    }


}
