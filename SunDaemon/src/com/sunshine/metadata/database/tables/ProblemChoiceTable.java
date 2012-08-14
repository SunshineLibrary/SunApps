package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.provider.MetadataContract.ProblemChoices;

public class ProblemChoiceTable extends DownloadableTable {
    public static final String TABLE_NAME = "problem_choices";

    public static final String[] ALL_COLUMNS = {
            ProblemChoices._ID,
            ProblemChoices._CHOICE,
            ProblemChoices._BODY,
            ProblemChoices._PARENT_ID
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {ProblemChoices._ID, "INTEGER PRIMARY KEY"},
            {ProblemChoices._CHOICE, "TEXT"},
            {ProblemChoices._BODY, "TEXT"},
            {ProblemChoices._PARENT_ID, "INTEGER"},
    };

    public ProblemChoiceTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
