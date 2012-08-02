package com.sunshine.metadata.database;

import android.content.Context;
import com.sunshine.metadata.database.observers.DownloadableTableObserver;
import com.sunshine.metadata.database.observers.TableObserver;
import com.sunshine.metadata.database.observers.ThumbnailFetchObserver;
import com.sunshine.metadata.database.tables.*;

public class MetadataDBHandlerFactory {
    public static MetadataDBHandler newMetadataDBHandler(Context context) {
        MetadataDBHandler dbHandler = new MetadataDBHandler(context);

        // Non-observable tables
        dbHandler.addTableManager(APISyncStateTable.TABLE_NAME, new APISyncStateTable(dbHandler));
        dbHandler.addTableManager(PackageTable.TABLE_NAME, new PackageTable(dbHandler));
        dbHandler.addTableManager(CourseTable.TABLE_NAME, new CourseTable(dbHandler));
        dbHandler.addTableManager(ChapterTable.TABLE_NAME, new ChapterTable(dbHandler));
        dbHandler.addTableManager(LessonTable.TABLE_NAME, new LessonTable(dbHandler));
        dbHandler.addTableManager(SectionTable.TABLE_NAME, new SectionTable(dbHandler));
        dbHandler.addTableManager(EdgeTable.TABLE_NAME, new EdgeTable(dbHandler));
        dbHandler.addTableManager(ProblemTable.TABLE_NAME, new ProblemTable(dbHandler));
        dbHandler.addTableManager(ProblemChoiceTable.TABLE_NAME, new ProblemChoiceTable(dbHandler));
        dbHandler.addTableManager(BookCollectionTable.TABLE_NAME, new BookCollectionTable(dbHandler));
        dbHandler.addTableManager(BookListTable.TABLE_NAME, new BookListTable(dbHandler));
        dbHandler.addTableManager(TagTable.TABLE_NAME, new TagTable(dbHandler));
        dbHandler.addTableManager(BookTagTable.TABLE_NAME, new BookTagTable(dbHandler));
        dbHandler.addTableManager(BookCollectionTagTable.TABLE_NAME, new BookCollectionTagTable(dbHandler));
        dbHandler.addTableManager(BookListTagTable.TABLE_NAME, new BookListTagTable(dbHandler));
        dbHandler.addTableManager(BookListCollectionTable.TABLE_NAME, new BookListCollectionTable(dbHandler));
        // Observable Tables
        TableObserver downloadableObserver = new DownloadableTableObserver(context);
        TableObserver thumbnailObserver = new ThumbnailFetchObserver(context);
        ObservableTable table = new ObservableTable(new ActivityTable(dbHandler));
        table.addObserver(downloadableObserver);
        table.addObserver(thumbnailObserver);
        dbHandler.addTableManager(ActivityTable.TABLE_NAME, table);

        table = new ObservableTable(new GalleryImageTable(dbHandler));
        table.addObserver(downloadableObserver);
        table.addObserver(thumbnailObserver);
        dbHandler.addTableManager(GalleryImageTable.TABLE_NAME, table);

        table = new ObservableTable(new BookTable(dbHandler));
        table.addObserver(downloadableObserver);
        dbHandler.addTableManager(BookTable.TABLE_NAME, table);

        return dbHandler;
    }
}
