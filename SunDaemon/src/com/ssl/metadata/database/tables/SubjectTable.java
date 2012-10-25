package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;

import static com.ssl.metadata.provider.MetadataContract.Subjects;

public class SubjectTable extends AbstractTable {

    public static final String TABLE_NAME = "subjects";

    private static final String[] ALL_COLUMNS = {
            Subjects._ID,
            Subjects._NAME,
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {Subjects._ID, "INTEGER PRIMARY KEY"},
            {Subjects._NAME, "TEXT"}
    };


    public SubjectTable(DBHandler handler) {
        super(handler, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
