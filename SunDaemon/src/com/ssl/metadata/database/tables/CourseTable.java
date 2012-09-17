package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;

import static com.ssl.metadata.provider.MetadataContract.Courses;

public class CourseTable extends AbstractTable {

    public static final String TABLE_NAME = "courses";

    private static final String[] ALL_COLUMNS = {
            Courses._ID,
            Courses._NAME,
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {Courses._ID, "INTEGER PRIMARY KEY"},
            {Courses._NAME, "TEXT"}
    };


    public CourseTable(DBHandler handler) {
        super(handler, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
