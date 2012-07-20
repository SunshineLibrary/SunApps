package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;

import static com.sunshine.metadata.provider.MetadataContract.Chapters;
import static com.sunshine.metadata.provider.MetadataContract.Courses;

public class ChapterTable extends MenuWithForeignKeyTable {

    public static final String TABLE_NAME = "chapters";

    private static final String[] ALL_COLUMNS = {
            Chapters._ID,
            Chapters._NAME,
            Chapters._PARENT_ID
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {Chapters._ID, "INTEGER PRIMARY KEY"},
            {Chapters._NAME, "TEXT"},
            {Chapters._PARENT_ID, "INTEGER NOT NULL"}
    };

    public ChapterTable(MetadataDBHandler handler) {
        super(handler);
    }

    @Override
    protected String getForeignKey() {
        return Courses._ID;
    }

    @Override
    protected String getParentTableName() {
        return CourseTable.TABLE_NAME;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[][] getColumnDefinitions() {
        return COLUMN_DEFINITIONS;
    }

    @Override
    public String[] getColumns() {
        return ALL_COLUMNS;
    }
}
