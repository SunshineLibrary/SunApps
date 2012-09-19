package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract.Activities;

public class ActivityTable extends DownloadableTable {
    public static final String TABLE_NAME = "activities";

    public static final String[] ALL_COLUMNS = {
            Activities._ID,
            Activities._PROVIDER_ID,
            Activities._TYPE,
            Activities._NAME,
            Activities._NOTES,
            Activities._DURATION,
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {Activities._ID, "INTEGER PRIMARY KEY"},
            {Activities._PROVIDER_ID, "INTEGER"},
            {Activities._TYPE, "INTEGER"},
            {Activities._NAME, "TEXT"},
            {Activities._NOTES, "TEXT"},
            {Activities._DURATION, "INTEGER"},
    };

    public ActivityTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
