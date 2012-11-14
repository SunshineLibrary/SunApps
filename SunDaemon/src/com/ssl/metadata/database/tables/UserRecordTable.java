package com.ssl.metadata.database.tables;

import android.provider.BaseColumns;
import com.ssl.metadata.database.DBHandler;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class UserRecordTable extends AbstractTable {

    public UserRecordTable(DBHandler handler, String tableName, String[][] columnDefinitions, String[] columns) {
        super(handler, tableName, columnDefinitions, columns);
    }

    public static final String TABLE_NAME = "user_records";

    public static final class UserRecord {
        public static final String _ID = BaseColumns._ID;
        public static final String _RECORD_TYPE = "record_type";
        public static final String _RECORD_PARAMS = "record_params";
    }

    public static final String[] ALL_COLUMNS = {
            UserRecord._ID,
            UserRecord._RECORD_TYPE,
            UserRecord._RECORD_PARAMS,
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {UserRecord._ID, "INTEGER PRIMARY KEY"},
            {UserRecord._RECORD_TYPE, "TEXT"},
            {UserRecord._RECORD_PARAMS, "TEXT"},
    };

    public UserRecordTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
