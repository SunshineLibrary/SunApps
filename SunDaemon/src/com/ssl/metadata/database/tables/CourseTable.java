package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract;

import static com.ssl.metadata.provider.MetadataContract.Courses;
import static com.ssl.metadata.provider.MetadataContract.Subjects;

public class CourseTable extends MenuWithForeignKeyTable {

    public static final String TABLE_NAME = "courses";

    private static final String[] ALL_COLUMNS = {
            Courses._ID,
            Courses._NAME,
            Courses._PARENT_ID,
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {Courses._ID, "INTEGER PRIMARY KEY"},
            {Courses._NAME, "TEXT"},
            {Courses._PARENT_ID, "INTEGER NOT NULL"},
    };


    public CourseTable(DBHandler handler) {
        super(handler, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }

    @Override
    protected String getForeignKey() {
        return Subjects._ID;
    }

    @Override
    protected String getParentTableName() {
        return SubjectTable.TABLE_NAME;
    }

    @Override
    protected String getForeignKeyColumn() {
        return Courses._PARENT_ID;
    }
}
