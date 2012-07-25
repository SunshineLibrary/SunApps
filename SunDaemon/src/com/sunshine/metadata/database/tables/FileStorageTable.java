package com.sunshine.metadata.database.tables;

import android.provider.BaseColumns;
import com.sunshine.metadata.database.MetadataDBHandler;

public class FileStorageTable extends Table {

    public static final String TABLE_NAME = "files";

    private static final class FileEntry {
        public static final String _ID = BaseColumns._ID;
        public static final String _URI = "uri";
        public static final String _SIZE = "size";
        public static final String _PATH = "path";
    }

    private static final String[] ALL_COLUMNS = {
            FileEntry._ID,
            FileEntry._URI,
            FileEntry._SIZE,
            FileEntry._PATH,
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {FileEntry._ID, "INTEGER PRIMARY KEY"},
            {FileEntry._URI, "TEXT NOT NULL"},
            {FileEntry._PATH, "TEXT NOT NULL"},
            {FileEntry._SIZE, "INTEGER NOT NULL"}
    };

    public FileStorageTable(MetadataDBHandler handler) {
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
