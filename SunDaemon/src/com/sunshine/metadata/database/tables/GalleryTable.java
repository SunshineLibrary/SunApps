package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract;

public class GalleryTable extends Table {
    public static final String TABLE_NAME = "gallery";

    private static final String[] ALL_COLUMNS = {
            MetadataContract.Gallery._ID,
            MetadataContract.Gallery._THUMBNAIL_PATH,
            MetadataContract.Gallery._IMAGE_PATH,
            MetadataContract.Gallery._DESCRIPTION
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {MetadataContract.Gallery._ID, "INTEGER PRIMARY KEY"},
            {MetadataContract.Gallery._THUMBNAIL_PATH, "TEXT"},
            {MetadataContract.Gallery._IMAGE_PATH, "TEXT"},
            {MetadataContract.Gallery._DESCRIPTION, "TEXT"}
    };

    public GalleryTable(MetadataDBHandler handler) {
        super(handler);
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
