package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract;

public class GalleryImageTable extends DownloadableTable {
    public static final String TABLE_NAME = "gallery_images";

    private static final String[] ALL_COLUMNS = {
            MetadataContract.GalleryImages._ID,
            MetadataContract.GalleryImages._GALLERY_ID,
            MetadataContract.GalleryImages._DESCRIPTION
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {MetadataContract.GalleryImages._ID, "INTEGER PRIMARY KEY"},
            {MetadataContract.GalleryImages._GALLERY_ID, "INTEGER"},
            {MetadataContract.GalleryImages._DESCRIPTION, "TEXT"}
    };

    public GalleryImageTable(MetadataDBHandler handler) {
        super(handler, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}