package com.ssl.metadata.database;

import android.content.Context;
import com.ssl.metadata.database.observers.DownloadableTableObserver;
import com.ssl.metadata.database.observers.TableObserver;
import com.ssl.metadata.database.observers.ThumbnailFetchObserver;
import com.ssl.metadata.database.observers.UserRecordUpdateObserver;
import com.ssl.metadata.database.tables.*;
import com.ssl.metadata.database.views.*;

public class MetadataDBHandlerFactory {

    private static final int DB_VERSION = 114;
    private static final String DB_NAME = "metadata";

    public static DBHandler newMetadataDBHandler(Context context) {
        DBHandler dbHandler = new DBHandler(context, DB_NAME, DB_VERSION);

        initSystemTables(dbHandler);
        initNormalTables(dbHandler);
        initObservableTables(dbHandler, context);
        initTableViews(dbHandler);

        return dbHandler;
    }

    private static void initSystemTables(DBHandler dbHandler) {
        dbHandler.addTableManager(APISyncStateTable.TABLE_NAME, new APISyncStateTable(dbHandler));
        dbHandler.addTableManager(PackageTable.TABLE_NAME, new PackageTable(dbHandler));
    }

    private static void initNormalTables(DBHandler dbHandler) {
        dbHandler.addTableManager(SubjectTable.TABLE_NAME, new SubjectTable(dbHandler));
        dbHandler.addTableManager(CourseTable.TABLE_NAME, new CourseTable(dbHandler));
        dbHandler.addTableManager(ChapterTable.TABLE_NAME, new ChapterTable(dbHandler));
        dbHandler.addTableManager(LessonTable.TABLE_NAME, new LessonTable(dbHandler));
        dbHandler.addTableManager(EdgeTable.TABLE_NAME, new EdgeTable(dbHandler));
        dbHandler.addTableManager(ProblemChoiceTable.TABLE_NAME, new ProblemChoiceTable(dbHandler));
        dbHandler.addTableManager(BookListTable.TABLE_NAME, new BookListTable(dbHandler));
        dbHandler.addTableManager(TagTable.TABLE_NAME, new TagTable(dbHandler));
        dbHandler.addTableManager(BookTagTable.TABLE_NAME, new BookTagTable(dbHandler));
        dbHandler.addTableManager(BookCollectionTagTable.TABLE_NAME, new BookCollectionTagTable(dbHandler));
        dbHandler.addTableManager(BookListTagTable.TABLE_NAME, new BookListTagTable(dbHandler));
        dbHandler.addTableManager(BookListCollectionTable.TABLE_NAME, new BookListCollectionTable(dbHandler));
        dbHandler.addTableManager(AuthorTable.TABLE_NAME, new AuthorTable(dbHandler));
        dbHandler.addTableManager(QuizComponentsTable.TABLE_NAME, new QuizComponentsTable(dbHandler));
        dbHandler.addTableManager(SectionComponentsTable.TABLE_NAME, new SectionComponentsTable(dbHandler));
        dbHandler.addTableManager(UserBookTable.TABLE_NAME, new UserBookTable(dbHandler));
    }

    private static void initObservableTables(DBHandler dbHandler, Context context){
        TableObserver downloadableObserver = new DownloadableTableObserver(context);
        TableObserver thumbnailObserver = new ThumbnailFetchObserver(context);
        TableObserver userRecordObserver = new UserRecordUpdateObserver(context);
        ObservableTable table = new ObservableTable(new ActivityTable(dbHandler));
        table.addObserver(downloadableObserver);
        //table.addObserver(thumbnailObserver);
        table.addObserver(userRecordObserver);
        dbHandler.addTableManager(ActivityTable.TABLE_NAME, table);

        table = new ObservableTable(new SectionTable(dbHandler));
        table.addObserver(downloadableObserver);
        table.addObserver(userRecordObserver);
        dbHandler.addTableManager(SectionTable.TABLE_NAME, table);

        table = new ObservableTable(new GalleryImageTable(dbHandler));
        table.addObserver(downloadableObserver);
        //table.addObserver(thumbnailObserver);
        dbHandler.addTableManager(GalleryImageTable.TABLE_NAME, table);

        table = new ObservableTable(new BookTable(dbHandler));
        table.addObserver(downloadableObserver);
        //table.addObserver(thumbnailObserver);
        table.addObserver(userRecordObserver);
        dbHandler.addTableManager(BookTable.TABLE_NAME, table);

        table = new ObservableTable(new BookCollectionTable(dbHandler));
        table.addObserver(downloadableObserver);
        //table.addObserver(thumbnailObserver);
        dbHandler.addTableManager(BookCollectionTable.TABLE_NAME, table);

        table = new ObservableTable(new ProblemTable(dbHandler));
        table.addObserver(userRecordObserver);
        dbHandler.addTableManager(ProblemTable.TABLE_NAME, table);

    }

    private static void initTableViews(DBHandler dbHandler) {
        dbHandler.addTableViewManager(SectionActivitiesView.VIEW_NAME, new SectionActivitiesView(dbHandler));
        dbHandler.addTableViewManager(QuizProblemsView.VIEW_NAME, new QuizProblemsView(dbHandler));
        dbHandler.addTableViewManager(BookInfoView.VIEW_NAME, new BookInfoView(dbHandler));
        dbHandler.addTableViewManager(BookCollectionInfoView.VIEW_NAME, new BookCollectionInfoView(dbHandler));
        dbHandler.addTableViewManager(BookCategoryView.VIEW_NAME, new BookCategoryView(dbHandler));
    }
}
