package com.sunshine.metadata.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sunshine.metadata.database.tables.*;

import java.util.HashMap;

/**
 * @author Bowen Sun
 *
 * Manages the meta-data database,
 */
public class MetadataDBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "metadb";
    private HashMap<String, Table> tableManagers;

    /**
     * @param context
     */
    public MetadataDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        tableManagers = new HashMap<String, Table>();
        initTables();
    }

    private void initTables() {
        setTableManager(APISyncStateTable.TABLE_NAME, new APISyncStateTable(this));
        setTableManager(PackageTable.TABLE_NAME, new PackageTable(this));
        setTableManager(BookTable.TABLE_NAME, new BookTable(this));
        setTableManager(CourseTable.TABLE_NAME, new CourseTable(this));
        setTableManager(ChapterTable.TABLE_NAME, new ChapterTable(this));
        setTableManager(LessonTable.TABLE_NAME, new LessonTable(this));
        setTableManager(SectionTable.TABLE_NAME, new SectionTable(this));
        setTableManager(ActivityTable.TABLE_NAME, new ActivityTable(this));
        setTableManager(GalleryTable.TABLE_NAME, new GalleryTable(this));
        setTableManager(EdgeTable.TABLE_NAME, new EdgeTable(this));
        setTableManager(ProblemTable.TABLE_NAME, new ProblemTable(this));
        setTableManager(ProblemChoiceTable.TABLE_NAME, new ProblemChoiceTable(this));
        setTableManager(BookTable.TABLE_NAME, new BookTable(this));
        setTableManager(BookCollectionTable.TABLE_NAME, new BookCollectionTable(this));
        setTableManager(BookListTable.TABLE_NAME, new BookListTable(this));

    }

    private void setTableManager(String tableName, Table table) {
        tableManagers.put(tableName, table);
    }

    public Table getTableManager(String tableName) {
        return tableManagers.get(tableName);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Table table : tableManagers.values()) {
            table.createTable(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        for (Table table : tableManagers.values()) {
            table.upgradeTable(db, oldVersion, newVersion);
        }
    }
}
