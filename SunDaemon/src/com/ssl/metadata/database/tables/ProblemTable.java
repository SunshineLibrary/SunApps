package com.ssl.metadata.database.tables;

import android.database.sqlite.SQLiteDatabase;
import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract.Problems;

public class ProblemTable extends DownloadableTable {
    public static final String TABLE_NAME = "problems";

    public static final String[] ALL_COLUMNS = {
            Problems._ID,
            Problems._BODY,
            Problems._TYPE,
            Problems._ANSWER,
            Problems._IS_CORRECT,
            Problems._USER_ANSWER,
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {Problems._ID, "INTEGER PRIMARY KEY"},
            {Problems._BODY, "TEXT"},
            {Problems._TYPE, "TEXT"},
            {Problems._ANSWER, "TEXT"},
            {Problems._USER_ANSWER, "TEXT"},
            {Problems._IS_CORRECT, "INTEGER"},
    };

    public ProblemTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }

    @Override
    public void upgradeTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        addColumn(db, oldVersion, 114, Problems._USER_ANSWER, "TEXT");
        addColumn(db, oldVersion, 114, Problems._IS_CORRECT, "INTEGER");
    }
}