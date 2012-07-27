package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.ProblemChoices;

public class ProblemChoiceTable extends DownloadableTable {
    public static final String TABLE_NAME = "activities";

    public static final String[] ALL_COLUMNS = {
            ProblemChoices._ID,
            ProblemChoices._CHOICE,
            ProblemChoices._BODY
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {ProblemChoices._ID, "INTEGER PRIMARY KEY"},
            {ProblemChoices._CHOICE, "TEXT"},
            {ProblemChoices._BODY, "TEXT"},
    };

    public ProblemChoiceTable(MetadataDBHandler db) {
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
