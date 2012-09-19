package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract.Problems;

public class ProblemTable extends DownloadableTable {
    public static final String TABLE_NAME = "problems";

    public static final String[] ALL_COLUMNS = {
            Problems._ID,
            Problems._BODY,
            Problems._TYPE,
            Problems._ANSWER,
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {Problems._ID, "INTEGER PRIMARY KEY"},
            {Problems._BODY, "TEXT"},
            {Problems._TYPE, "TEXT"},
            {Problems._ANSWER, "TEXT"},
    };

    public ProblemTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}