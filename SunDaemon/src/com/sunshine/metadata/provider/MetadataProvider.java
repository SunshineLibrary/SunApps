package com.sunshine.metadata.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.MetadataDBHandlerFactory;
import com.sunshine.metadata.database.tables.*;
import com.sunshine.support.storage.SharedStorageManager;

import java.io.FileNotFoundException;

import static com.sunshine.metadata.provider.MetadataContract.SectionComponents;

public class MetadataProvider extends ContentProvider {

    private static final String AUTHORITY = MetadataContract.AUTHORITY;

    private static final UriMatcher sUriMatcher = Matcher.Factory.getMatcher();

    private DBHandler dbHandler;

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
            case Matcher.SECTIONS_ACTIVITIES:
                return dbHandler.getTableViewManager(SectionActivitiesView.VIEW_NAME).query(
                        uri, projection, SectionComponents._SECTION_ID + "=?",
                        new String[]{uri.getLastPathSegment()}, sortOrder);
            case Matcher.PROBLEMS_BELONG_TO_QUIZ_ACTIVITY:
                return dbHandler.getTableManager(QuizComponentsTable.TABLE_NAME).query(
                        uri, projection, MetadataContract.QuizComponents._QUIZ_ACTIVITY_ID + "=?",
                        new String[]{uri.getLastPathSegment()}, sortOrder);
            case Matcher.GALLERY_IMAGES:
                return dbHandler.getTableManager(GalleryImageTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.ACTIVITIES:
                return dbHandler.getTableManager(ActivityTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.EDGES:
                return dbHandler.getTableManager(EdgeTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.ACTIVITIES_ID:
                return dbHandler.getTableManager(ActivityTable.TABLE_NAME).query(
                        uri, projection, BaseColumns._ID + "=?",
                        new String[]{uri.getLastPathSegment()}, sortOrder);
            case Matcher.AUTHORS:
            	return dbHandler.getTableManager(AuthorTable.TABLE_NAME).query(
            			uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.BOOK_INFO:
            	return dbHandler.getTableViewManager(BookInfoView.VIEW_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.BOOKS:
                return dbHandler.getTableManager(BookTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.BOOK_COLLECTIONS:
                return dbHandler.getTableManager(BookCollectionTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.BOOK_LISTS:
                return dbHandler.getTableManager(BookListTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.TAGS:
                return dbHandler.getTableManager(TagTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.BOOK_LIST_TAG:
                return dbHandler.getTableManager(BookListTagTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.BOOK_COLLECTION_TAG:
                return dbHandler.getTableManager(BookCollectionTagTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.BOOK_LIST_COLLECTION:
                return dbHandler.getTableManager(BookListCollectionTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.PROBLEMS:
                return dbHandler.getTableManager(ProblemTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.PROBLEMS_CHOICE:
                return dbHandler.getTableManager(ProblemChoiceTable.TABLE_NAME).query(
                        uri, projection, selection, selectionArgs, sortOrder);
            case Matcher.USER_BOOK:
            	return dbHandler.getTableManager(UserBookTable.TABLE_NAME).query( 
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
            case Matcher.USER_BOOK:
            	return dbHandler.getTableManager(UserBookTable.TABLE_NAME).insert(
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
                selectionArgs = new String[]{uri.getLastPathSegment()};
            case Matcher.ACTIVITIES:
                return dbHandler.getTableManager(ActivityTable.TABLE_NAME).update(
                        uri, values, selection, selectionArgs);
            case Matcher.GALLERY_IMAGES_ID:
                selection = MetadataContract.GalleryImages._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
            case Matcher.GALLERY_IMAGES:
                return dbHandler.getTableManager(GalleryImageTable.TABLE_NAME).update(
                        uri, values, selection, selectionArgs);
            case Matcher.BOOKS_ID:
            	selection = MetadataContract.Books._ID + " = ?";
            	selectionArgs = new String[]{uri.getLastPathSegment()};
            case Matcher.BOOKS:
            	return dbHandler.getTableManager(BookTable.TABLE_NAME).update(
                        uri, values, selection, selectionArgs);
            case Matcher.USER_BOOK_ID:
            	selection = MetadataContract.UserBook._ID + " = ?";
            	selectionArgs = new String[]{uri.getLastPathSegment()};
            case Matcher.USER_BOOK:
            	return dbHandler.getTableManager(UserBookTable.TABLE_NAME).update(
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
