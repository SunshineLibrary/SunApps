package com.sunshine.metadata.database.tables;

import android.provider.BaseColumns;
import com.sunshine.metadata.database.DBHandler;

/**
 * @author Bowen Sun
 *
 */
public class APISyncStateTable extends AbstractTable {

    public static final String TABLE_NAME = "api_sync_states";

    public static final class APISyncState {
        public static final String _ID = BaseColumns._ID;
        public static final String _TABLE_NAME = "table_name";
        public static final String _LAST_UPDATE = "last_update";
        public static final String _LAST_SYNC = "last_sync";
        public static final String _LAST_SYNC_STATUS = "last_sync_status";

        public static final int SYNC_SUCCESS = 0;
        public static final int SYNC_FAILURE= 1;
        public static final int SYNC_ONGOING= 2;
    }

    public static final String[] ALL_COLUMNS = {
            APISyncState._ID,
            APISyncState._TABLE_NAME,
            APISyncState._LAST_UPDATE,
            APISyncState._LAST_SYNC,
            APISyncState._LAST_SYNC_STATUS
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {APISyncState._ID, "INTEGER PRIMARY KEY"},
            {APISyncState._TABLE_NAME, "TEXT"},
            {APISyncState._LAST_UPDATE, "INTEGER"},
            {APISyncState._LAST_SYNC, "INTEGER"},
            {APISyncState._LAST_SYNC_STATUS, "INTEGER"},
    };

    public APISyncStateTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}