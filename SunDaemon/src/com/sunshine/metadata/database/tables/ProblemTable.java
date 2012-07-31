package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.Problems;

public class ProblemTable extends DownloadableTable {
    public static final String TABLE_NAME = "problems";

    public static final String[] ALL_COLUMNS = {
            Problems._ID,
            Problems._BODY,
            Problems._TYPE,
            Problems._TITLE,
            Problems._ANSWER
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {Problems._ID, "INTEGER PRIMARY KEY"},
            {Problems._BODY, "TEXT"},
            {Problems._TYPE, "TEXT"},
            {Problems._TITLE, "TEXT"},
            {Problems._ANSWER, "TEXT"}
    };

    public ProblemTable(MetadataDBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
