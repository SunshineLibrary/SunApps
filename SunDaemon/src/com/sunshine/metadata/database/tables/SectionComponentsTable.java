package com.sunshine.metadata.database.tables;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract;

public class SectionComponentsTable extends AbstractTable {

    private static final class SectionComponents {
        public static final String _ID = BaseColumns._ID;
        public static final String _SECTION_ID = "section_id";
        public static final String _ACTIVITY_ID = "activity_id";
        public static final String _SEQUENCE = MetadataContract.Activities._SEQUENCE;
    }

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

    public SectionComponentsTable(MetadataDBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
