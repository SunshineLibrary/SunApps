package com.ssl.metadata.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.database.Table;
import com.ssl.metadata.database.TableView;
import com.ssl.metadata.database.tables.*;
import com.ssl.metadata.database.views.*;
import com.ssl.metadata.storage.SharedStorageManager;
import com.ssl.support.application.DaemonApplication;
import org.apache.http.impl.auth.DigestSchemeFactory;

import java.io.FileNotFoundException;

import static com.ssl.metadata.provider.MetadataContract.SectionComponents;

public class MetadataProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = Matcher.Factory.getMatcher();

    private static final String PASSWORD = "alife";

    private DBHandler dbHandler;
    private SharedStorageManager sharedStorageManager;

    private Table packageTable;
    private Table fileTable;
    private Table apiSyncStateTable;
    private Table subjectTable;
    private Table courseTable;
    private Table chapterTable;
    private Table lessonTable;
    private Table sectionTable;
    private TableView sectionsActivitiesView;
    private Table galleryImagesTable;
    private Table activityTable;
    private Table edgeTable;
    private Table authorTable;
    private TableView bookInfoView;
    private Table bookTable;
    private Table bookCollectionTable;
    private TableView bookCollectionInfoView;
    private Table bookListTable;
    private Table tagTable;
    private Table bookListTagTable;
    private Table bookCollectionTagTable;
    private Table bookListCollectionTable;
    private TableView bookCategoryView;
    private Table problemTable;
    private Table problemChoiceTable;
    private Table userBookTable;
    private Table quizComponentsTable;
    private TableView quizProblemsView;


    @Override
    public boolean onCreate() {
        dbHandler = ((DaemonApplication) getContext().getApplicationContext()).getMetadataDBHandler();
        fileTable = dbHandler.getTableManager(FileTable.TABLE_NAME);
        sharedStorageManager = new SharedStorageManager(getContext(), (FileTable) fileTable);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriMatch = sUriMatcher.match(uri);
        if (uriMatch == Matcher.SYSTEM_PASSWORD) {
            return getNewPasswordCursor();
        }

        Table table = getTableForMatch(uriMatch);
        TableView view = getViewForMatch(uriMatch);

        if (table == null && view == null) {
            throw new IllegalArgumentException();
        }

        switch (uriMatch) {
            // Views
            case Matcher.SECTIONS_ACTIVITIES:
                return view.query(uri, projection,
                        SectionComponents._SECTION_ID + "=?", new String[]{uri.getLastPathSegment()}, sortOrder);

            case Matcher.QUIZ_PROBLEMS:
            case Matcher.BOOK_INFO:
            case Matcher.BOOK_COLLECTION_INFO:
            case Matcher.BOOK_CATEGORY:
            	return view.query(uri, projection, selection, selectionArgs, sortOrder);

            // Collections
            case Matcher.PACKAGES:
            case Matcher.FILES:
            case Matcher.API_SYNC_STATES:
            case Matcher.SUBJECTS:
            case Matcher.COURSES:
            case Matcher.CHAPTERS:
            case Matcher.LESSONS:
            case Matcher.SECTIONS:
            case Matcher.GALLERY_IMAGES:
            case Matcher.ACTIVITIES:
            case Matcher.EDGES:
            case Matcher.AUTHORS:
            case Matcher.BOOKS:
            case Matcher.BOOK_COLLECTIONS:            
            case Matcher.BOOK_LISTS:
            case Matcher.TAGS:
            case Matcher.BOOK_LIST_TAG:
            case Matcher.BOOK_COLLECTION_TAG:
            case Matcher.BOOK_LIST_COLLECTION:            
            case Matcher.PROBLEMS:
            case Matcher.QUIZ_COMPONENTS:
            case Matcher.PROBLEM_CHOICES:
            case Matcher.USER_BOOK:
                return table.query(uri, projection, selection, selectionArgs, sortOrder);

            // Elements
            case Matcher.ACTIVITIES_ID:
            case Matcher.PACKAGES_ID:
            case Matcher.SECTIONS_ID:
            case Matcher.PROBLEMS_ID:
                return table.query(uri, projection, BaseColumns._ID + "=" + uri.getLastPathSegment(), null, null);

            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case Matcher.PACKAGES:
            case Matcher.API_SYNC_STATES:
            case Matcher.SUBJECTS:
            case Matcher.COURSES:
            case Matcher.CHAPTERS:
            case Matcher.LESSONS:
            case Matcher.SECTIONS:
            case Matcher.ACTIVITIES:
            case Matcher.EDGES:
            case Matcher.AUTHORS:
            case Matcher.BOOK_INFO:
            case Matcher.BOOKS:
            case Matcher.BOOK_COLLECTIONS:
            case Matcher.BOOK_COLLECTION_INFO:
            case Matcher.BOOK_LISTS:
            case Matcher.TAGS:
            case Matcher.BOOK_LIST_TAG:
            case Matcher.BOOK_COLLECTION_TAG:
            case Matcher.BOOK_LIST_COLLECTION:
            case Matcher.BOOK_CATEGORY:
            case Matcher.PROBLEMS:
            case Matcher.PROBLEM_CHOICES:
                return MimeType.METADATA_MIME_TYPE;
            case Matcher.GALLERY_IMAGES:
                return MimeType.GALLERY_IMAGES_MIME_TYPE;
            default:
                return sharedStorageManager.getType(uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriMatch = sUriMatcher.match(uri);
        Table table = getTableForMatch(uriMatch);

        if (table == null) {
            throw new IllegalArgumentException();
        }

        switch (uriMatch) {
            case Matcher.GALLERY_IMAGES:
            case Matcher.ACTIVITIES:
            case Matcher.API_SYNC_STATES:
            case Matcher.PACKAGES:
                return table.insert(uri, values);
            case Matcher.FILES:
                return insertFilePath(values);
            default:
                throw new IllegalArgumentException();
        }
    }

    private Uri insertFilePath(ContentValues values) {
        String uriPath, fileName, fileExtension, filePath;
        uriPath = values.getAsString(MetadataContract.Files._URI_PATH);
        fileName = values.getAsString(MetadataContract.Files._FILE_PATH);

        if (!uriPath.startsWith("/") || fileName.lastIndexOf(".") < 0) {
            return null;
        }

        fileExtension = fileName.substring(fileName.lastIndexOf("."));
        fileName = uriPath.substring(1).replace("/", "_");

        filePath = sharedStorageManager.moveFile(uriPath, fileName + fileExtension);
        values.put(MetadataContract.Files._FILE_PATH, filePath);

        Cursor cursor = fileTable.query(MetadataContract.Files.CONTENT_URI, null,
                MetadataContract.Files._URI_PATH + "='" + uriPath + "'", null, null);
        if (cursor.moveToFirst()) {
            fileTable.update(MetadataContract.Files.CONTENT_URI, values,
                    MetadataContract.Files._URI_PATH + "='" + uriPath + "'", null);
        } else {
            fileTable.insert(MetadataContract.Files.CONTENT_URI, values);
        }
        cursor.close();
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        getTableForMatch(match);
        switch (match) {
            case Matcher.PACKAGES:
                return packageTable.delete(uri, selection, selectionArgs);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriMatch = sUriMatcher.match(uri);
        Table table = getTableForMatch(uriMatch);

        if (table == null) {
            throw new IllegalArgumentException();
        }

        switch (uriMatch) {
            case Matcher.ACTIVITIES_ID:
            case Matcher.BOOKS_ID:
            case Matcher.GALLERY_IMAGES_ID:
            case Matcher.PACKAGES_ID:
            case Matcher.SECTIONS_ID:
            case Matcher.PROBLEMS_ID:
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
            case Matcher.PACKAGES:
            case Matcher.ACTIVITIES:
            case Matcher.GALLERY_IMAGES:
            case Matcher.BOOKS:
            case Matcher.API_SYNC_STATES:
            case Matcher.PROBLEMS:
                return table.update(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return sharedStorageManager.openFile(uri, mode);
    }

    private Table getTableForMatch(int match) {
        switch (match) {
            case Matcher.PACKAGES:
            case Matcher.PACKAGES_ID:
                if (packageTable == null) {
                    packageTable = dbHandler.getTableManager(PackageTable.TABLE_NAME);
                }
                return packageTable;
            case Matcher.FILES:
                if (fileTable == null) {
                    fileTable = dbHandler.getTableManager(FileTable.TABLE_NAME);
                }
                return fileTable;
            case Matcher.API_SYNC_STATES:
                if (apiSyncStateTable == null) {
                    apiSyncStateTable = dbHandler.getTableManager(APISyncStateTable.TABLE_NAME);
                }
                return apiSyncStateTable;
            case Matcher.SUBJECTS:
                if (subjectTable== null) {
                    subjectTable= dbHandler.getTableManager(SubjectTable.TABLE_NAME);
                }
                return subjectTable;
            case Matcher.COURSES:
                if (courseTable == null) {
                    courseTable = dbHandler.getTableManager(CourseTable.TABLE_NAME);
                }
                return courseTable;
            case Matcher.CHAPTERS:
                if (chapterTable == null) {
                    chapterTable = dbHandler.getTableManager(ChapterTable.TABLE_NAME);
                }
                return chapterTable;
            case Matcher.LESSONS:
                if (lessonTable == null) {
                    lessonTable = dbHandler.getTableManager(LessonTable.TABLE_NAME);
                }
                return lessonTable;
            case Matcher.SECTIONS:
            case Matcher.SECTIONS_ID:
                if (sectionTable == null) {
                    sectionTable = dbHandler.getTableManager(SectionTable.TABLE_NAME);
                }
                return sectionTable;
            case Matcher.GALLERY_IMAGES:
            case Matcher.GALLERY_IMAGES_ID:
                if (galleryImagesTable == null) {
                    galleryImagesTable = dbHandler.getTableManager(GalleryImageTable.TABLE_NAME);
                }
                return galleryImagesTable;
            case Matcher.ACTIVITIES:
            case Matcher.ACTIVITIES_ID:
                if (activityTable == null) {
                    activityTable = dbHandler.getTableManager(ActivityTable.TABLE_NAME);
                }
                return activityTable;
            case Matcher.EDGES:
                if (edgeTable == null) {
                    edgeTable = dbHandler.getTableManager(EdgeTable.TABLE_NAME);
                }
                return edgeTable;
            case Matcher.AUTHORS:
                if (authorTable == null) {
                    authorTable = dbHandler.getTableManager(AuthorTable.TABLE_NAME);
                }
                return authorTable;
            case Matcher.BOOKS:
            case Matcher.BOOKS_ID:
                if (bookTable == null) {
                    bookTable = dbHandler.getTableManager(BookTable.TABLE_NAME);
                }
                return bookTable;
            case Matcher.BOOK_COLLECTIONS:
                if (bookCollectionTable == null) {
                    bookCollectionTable = dbHandler.getTableManager(BookCollectionTable.TABLE_NAME);
                }
                return bookCollectionTable;
            case Matcher.BOOK_LISTS:
                if (bookListTable == null) {
                    bookListTable = dbHandler.getTableManager(BookListTable.TABLE_NAME);
                }
                return bookListTable;
            case Matcher.TAGS:
                if (tagTable == null) {
                    tagTable = dbHandler.getTableManager(TagTable.TABLE_NAME);
                }
                return tagTable;
            case Matcher.BOOK_LIST_TAG:
                if (bookListTagTable == null) {
                    bookListTagTable = dbHandler.getTableManager(BookListTagTable.TABLE_NAME);
                }
                return bookListTagTable;
            case Matcher.BOOK_COLLECTION_TAG:
                if (bookCollectionTagTable == null) {
                    bookCollectionTagTable = dbHandler.getTableManager(BookCollectionTagTable.TABLE_NAME);
                }
                return bookCollectionTagTable;
            case Matcher.BOOK_LIST_COLLECTION:
                if (bookListCollectionTable == null) {
                    bookListCollectionTable = dbHandler.getTableManager(BookListCollectionTable.TABLE_NAME);
                }
                return bookListCollectionTable;
//            case Matcher.BOOK_INFO:
//            	if (bookInfoView == null) {
//            		bookInfoView = dbHandler.getTableViewManager(BookInfoView.VIEW_NAME);
//                }
//                return bookInfoView;
            case Matcher.PROBLEMS:
            case Matcher.PROBLEMS_ID:
                if (problemTable == null) {
                    problemTable = dbHandler.getTableManager(ProblemTable.TABLE_NAME);
                }
                return problemTable;
            case Matcher.QUIZ_COMPONENTS:
                if (quizComponentsTable == null) {
                    quizComponentsTable = dbHandler.getTableManager(QuizComponentsTable.TABLE_NAME);
                }
                return quizComponentsTable;
            case Matcher.PROBLEM_CHOICES:
                if (problemChoiceTable == null) {
                    problemChoiceTable = dbHandler.getTableManager(ProblemChoiceTable.TABLE_NAME);
                }
                return problemChoiceTable;
            case Matcher.USER_BOOK:
                if (userBookTable == null) {
                    userBookTable = dbHandler.getTableManager(UserBookTable.TABLE_NAME);
                }
                return userBookTable;
            default:
            	return null;
        }
    }

    private TableView getViewForMatch(int match) {
        switch (match) {
            case Matcher.SECTIONS_ACTIVITIES:
                if (sectionsActivitiesView == null) {
                    sectionsActivitiesView = dbHandler.getTableViewManager(SectionActivitiesView.VIEW_NAME);
                }
                return sectionsActivitiesView;
            case Matcher.BOOK_INFO:
                if (bookInfoView == null) {
                    bookInfoView = dbHandler.getTableViewManager(BookInfoView.VIEW_NAME);
                }
                return bookInfoView;
            case Matcher.BOOK_COLLECTION_INFO:
                if (bookCollectionInfoView == null) {
                    bookCollectionInfoView = dbHandler.getTableViewManager(BookCollectionInfoView.VIEW_NAME);
                }
                return bookCollectionInfoView;
            case Matcher.BOOK_CATEGORY:
                if (bookCategoryView == null) {
                    bookCategoryView = dbHandler.getTableViewManager(BookCategoryView.VIEW_NAME);
                }
                return bookCategoryView;
            case Matcher.QUIZ_PROBLEMS:
                if (quizProblemsView == null) {
                    quizProblemsView = dbHandler.getTableViewManager(QuizProblemsView.VIEW_NAME);
                }
                return quizProblemsView;
            default:
                return null;
        }
    }

    public Cursor getNewPasswordCursor() {
        MatrixCursor cursor = new MatrixCursor(new String[] {"system_password"});
        cursor.addRow(new String[] {String.valueOf(PASSWORD.hashCode())});
        return cursor;
    }
}
