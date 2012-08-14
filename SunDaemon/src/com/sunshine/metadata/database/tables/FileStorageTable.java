package com.sunshine.metadata.database.tables;

import android.provider.BaseColumns;
import com.sunshine.metadata.database.DBHandler;

public class FileStorageTable extends AbstractTable {

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

    public FileStorageTable(DBHandler handler) {
        super(handler, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
