package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.provider.MetadataContract.Edges;

public class EdgeTable extends DownloadableTable {
    public static final String TABLE_NAME = "edges";

    public static final String[] ALL_COLUMNS = {
            Edges._ID,
            Edges._FROM_ID,
            Edges._TO_ID,
            Edges._CONDITION,
            Edges._SECTION_ID
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {Edges._ID, "INTEGER PRIMARY KEY"},
            {Edges._FROM_ID, "INTEGER"},
            {Edges._TO_ID, "INTEGER"},
            {Edges._CONDITION, "TEXT"},
            {Edges._SECTION_ID, "INTEGER"}
    };

    public EdgeTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
