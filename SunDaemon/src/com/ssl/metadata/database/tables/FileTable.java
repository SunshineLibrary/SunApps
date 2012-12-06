package com.ssl.metadata.database.tables;

import android.database.sqlite.SQLiteDatabase;
import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.metadata.provider.MetadataContract.Edges;

import static com.ssl.metadata.provider.MetadataContract.Files;

public class FileTable extends DownloadableTable {
    public static final String TABLE_NAME = "files";

    public static final String[] ALL_COLUMNS = {
            Files._ID,
            Files._URI_PATH,
            Files._FILE_PATH,
            Files._CONTENT_LENGTH,
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {Files._ID, "INTEGER PRIMARY KEY"},
            {Files._URI_PATH, "TEXT"},
            {Files._FILE_PATH, "TEXT"},
            {Files._CONTENT_LENGTH, "INTEGER"},
    };

    public FileTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }

    @Override
    public void upgradeTableInSteps(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.upgradeTableInSteps(db, oldVersion, newVersion);
        if (oldVersion < 116) {
            createTable(db);
        }
    }
}
