package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;

import static com.ssl.metadata.provider.MetadataContract.SectionComponents;

public class SectionComponentsTable extends AbstractTable {

    public static final String TABLE_NAME = "section_components";

    public static final String[] ALL_COLUMNS = {
            SectionComponents._ID,
            SectionComponents._SECTION_ID,
            SectionComponents._ACTIVITY_ID,
            SectionComponents._SEQUENCE,
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {SectionComponents._ID, "INTEGER PRIMARY KEY"},
            {SectionComponents._SECTION_ID, "INTEGER"},
            {SectionComponents._ACTIVITY_ID, "INTEGER"},
            {SectionComponents._SEQUENCE, "INTEGER"},
    };

    public SectionComponentsTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
