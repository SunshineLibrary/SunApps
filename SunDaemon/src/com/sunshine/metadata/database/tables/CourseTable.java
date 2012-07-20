package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;

import static com.sunshine.metadata.provider.MetadataContract.Courses;

public class CourseTable extends Table {

    public static final String TABLE_NAME = "courses";

    private static final String[] ALL_COLUMNS = {
            Courses._ID,
            Courses._NAME,
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {Courses._ID, "INTEGER PRIMARY KEY"},
            {Courses._NAME, "TEXT"}
    };


    public CourseTable(MetadataDBHandler handler) {
        super(handler);
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
