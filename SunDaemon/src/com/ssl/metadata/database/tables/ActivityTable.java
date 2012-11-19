package com.ssl.metadata.database.tables;

import android.database.sqlite.SQLiteDatabase;
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
            Activities._RESULT,
            Activities._STATUS,
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {Activities._ID, "INTEGER PRIMARY KEY"},
            {Activities._PROVIDER_ID, "INTEGER"},
            {Activities._TYPE, "INTEGER"},
            {Activities._NAME, "TEXT"},
            {Activities._NOTES, "TEXT"},
            {Activities._DURATION, "INTEGER"},
            {Activities._RESULT, "TEXT"},
            {Activities._STATUS, "TEXT"},
    };

    public ActivityTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }

    @Override
    public void upgradeTableInSteps(SQLiteDatabase db, int oldVersion, int newVersion) {
        addColumn(db, oldVersion, 114, Activities._RESULT, "TEXT");
        addColumn(db, oldVersion, 114, Activities._STATUS, "TEXT");
    }
}
