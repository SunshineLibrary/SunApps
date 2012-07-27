package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract;

public class GalleryTable extends Table {
    public static final String TABLE_NAME = "gallery";

    private static final String[] ALL_COLUMNS = {
            MetadataContract.GalleryImages._ID,
            MetadataContract.GalleryImages._GALLERY_ID,
            MetadataContract.GalleryImages._THUMBNAIL_PATH,
            MetadataContract.GalleryImages._IMAGE_PATH,
            MetadataContract.GalleryImages._DESCRIPTION
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {MetadataContract.GalleryImages._ID, "INTEGER PRIMARY KEY"},
            {MetadataContract.GalleryImages._GALLERY_ID, "INTEGER"},
            {MetadataContract.GalleryImages._THUMBNAIL_PATH, "TEXT"},
            {MetadataContract.GalleryImages._IMAGE_PATH, "TEXT"},
            {MetadataContract.GalleryImages._DESCRIPTION, "TEXT"}
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
