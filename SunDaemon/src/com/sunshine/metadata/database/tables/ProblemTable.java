package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.Problems;

public class ProblemTable extends DownloadableTable {
    public static final String TABLE_NAME = "problems";

    public static final String[] ALL_COLUMNS = {
            Problems._ID,
            Problems._BODY,
            Problems._TYPE,
            Problems._ANSWER,
            Problems._PARENT_ID
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {Problems._ID, "INTEGER PRIMARY KEY"},
            {Problems._BODY, "TEXT"},
            {Problems._TYPE, "TEXT"},
            {Problems._ANSWER, "TEXT"},
            {Problems._PARENT_ID,"INTEGER"}
    };

    public ProblemTable(MetadataDBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
