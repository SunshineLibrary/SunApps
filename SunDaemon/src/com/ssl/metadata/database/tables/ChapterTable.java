package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;

import static com.ssl.metadata.provider.MetadataContract.Chapters;
import static com.ssl.metadata.provider.MetadataContract.Courses;

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

    public ChapterTable(DBHandler handler) {
        super(handler, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
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
	protected String getForeignKeyColumn() {
		return Chapters._PARENT_ID;
	}
}
