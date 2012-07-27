package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.Problems;

public class ProblemTable extends DownloadableTable {
    public static final String TABLE_NAME = "problems";

    public static final String[] ALL_COLUMNS = {
            Problems._ID,
            Problems._BODY,
            Problems._TIPE,
            Problems._ANSWER
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {Problems._ID, "INTEGER PRIMARY KEY"},
            {Problems._BODY, "TEXT"},
            {Problems._TIPE, "TEXT"},
            {Problems._ANSWER, "TEXT"}
    };

    public ProblemTable(MetadataDBHandler db) {
        super(db);
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
