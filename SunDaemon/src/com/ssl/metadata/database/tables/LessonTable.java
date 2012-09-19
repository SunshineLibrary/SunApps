package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract;

import static com.ssl.metadata.provider.MetadataContract.Lessons;

public class LessonTable extends MenuWithForeignKeyTable {
    public static final String TABLE_NAME = "lessons";

    private static final String[] ALL_COLUMNS = {
            Lessons._ID,
            Lessons._NAME,
            Lessons._PARENT_ID
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {Lessons._ID, "INTEGER PRIMARY KEY"},
            {Lessons._NAME, "TEXT"},
            {Lessons._PARENT_ID, "INTEGER NOT NULL"}
    };

    public LessonTable(DBHandler handler) {
        super(handler, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }

    @Override
    protected String getForeignKey() {
        return MetadataContract.Chapters._ID;
    }

    @Override
    protected String getParentTableName() {
        return ChapterTable.TABLE_NAME;
    }

	@Override
	protected String getForeignKeyColumn() {
		return MetadataContract.Lessons._PARENT_ID;
	}
}
