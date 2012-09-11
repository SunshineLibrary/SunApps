package com.sunshine.support.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import com.sunshine.metadata.database.tables.APISyncStateTable;

import static com.sunshine.metadata.database.tables.APISyncStateTable.ApiSyncStates.*;

public class ApiSyncState {

    public long id;
    public String tableName;
    public long lastUpdateTime;
    public long lastSyncTime;
    public int lastSyncStatus;

    public ApiSyncState(long id, String tableName) {
        this.id = id;
        this.tableName = tableName;
    }
}
