package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.Activities;

public class ActivityTable extends DownloadableTable {
    public static final String TABLE_NAME = "activities";

    public static final String[] ALL_COLUMNS = {
            Activities._ID,
            Activities._SECTION_ID,
            Activities._PROVIDER_ID,
            Activities._TYPE,
            Activities._SEQUENCE,
            Activities._NAME,
            Activities._NOTES,
            Activities._LENGTH,
            Activities._DIFFICULTY,
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {Activities._ID, "INTEGER PRIMARY KEY"},
            {Activities._SECTION_ID, "INTEGER"},
            {Activities._PROVIDER_ID, "INTEGER"},
            {Activities._TYPE, "INTEGER"},
            {Activities._SEQUENCE, "INTEGER"},
            {Activities._NAME, "TEXT"},
            {Activities._NOTES, "TEXT"},
            {Activities._LENGTH, "INTEGER"},
            {Activities._DIFFICULTY, "INTEGER"},
    };

    public ActivityTable(MetadataDBHandler db) {
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
