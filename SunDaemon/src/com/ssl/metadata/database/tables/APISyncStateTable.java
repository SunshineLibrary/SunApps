package com.ssl.metadata.database.tables;

import android.net.Uri;
import android.provider.BaseColumns;
import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract;

/**
 * @author Bowen Sun
 *
 */
public class APISyncStateTable extends AbstractTable {

    public static final String TABLE_NAME = "api_sync_states";

    public static final class ApiSyncStates {
        public static final String _ID = BaseColumns._ID;
        public static final String _TABLE_NAME = "table_name";
        public static final String _LAST_UPDATE = "last_update";
        public static final String _LAST_SYNC = "last_sync";
        public static final String _LAST_SYNC_STATUS = "last_sync_status";

        public static final int SYNC_SUCCESS = 0;
        public static final int SYNC_FAILURE= 1;
        public static final int SYNC_ONGOING= 2;

        public static Uri CONTENT_URI = MetadataContract.AUTHORITY_URI.buildUpon().appendPath("api_sync_states").build();
    }

    public static final String[] ALL_COLUMNS = {
            ApiSyncStates._ID,
            ApiSyncStates._TABLE_NAME,
            ApiSyncStates._LAST_UPDATE,
            ApiSyncStates._LAST_SYNC,
            ApiSyncStates._LAST_SYNC_STATUS
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {ApiSyncStates._ID, "INTEGER PRIMARY KEY"},
            {ApiSyncStates._TABLE_NAME, "TEXT"},
            {ApiSyncStates._LAST_UPDATE, "INTEGER"},
            {ApiSyncStates._LAST_SYNC, "INTEGER"},
            {ApiSyncStates._LAST_SYNC_STATUS, "INTEGER"},
    };

    public APISyncStateTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}