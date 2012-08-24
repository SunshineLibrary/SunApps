package com.sunshine.support.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import com.sunshine.metadata.database.tables.APISyncStateTable;

import static com.sunshine.metadata.database.tables.APISyncStateTable.ApiSyncStates.*;

public class ApiSyncState implements PersistentData {

    private long id;
    private String tableName;
    private long lastUpdateTime;
    private long lastSyncTime;
    private int lastSyncStatus;
    private boolean isDirty;

    public ApiSyncState(String tableName) {
        this.tableName = tableName;
        this.id = -1;
    }

    public long getId() {
        return id;
    }

    public String getTableName() {
        return tableName;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public long getLastSyncTime() {
        return lastSyncTime;
    }

    public int getLastSyncStatus() {
        return lastSyncStatus;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        if (this.lastUpdateTime != lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
            isDirty = true;
        }
    }

    public void setLastSyncTime(long lastSyncTime) {
        if (this.lastSyncTime != lastSyncTime) {
            this.lastSyncTime = lastSyncTime;
            isDirty = true;
        }
    }

    public void setLastSyncStatus(int lastSyncStatus) {
        if (this.lastSyncStatus != lastSyncStatus) {
            this.lastSyncStatus = lastSyncStatus;
            isDirty = true;
        }
    }

    @Override
    public boolean isNew() {
        return id == -1;
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public ContentValues getUpdateContentValues() {
        ContentValues values = new ContentValues();
        values.put(_LAST_SYNC_STATUS, lastSyncStatus);
        values.put(_LAST_SYNC, lastSyncTime);
        values.put(_LAST_UPDATE, lastUpdateTime);
        return values;
    }

    @Override
    public ContentValues getCreateContentValues() {
        ContentValues values = new ContentValues();
        values.put(_TABLE_NAME, tableName);
        values.put(_LAST_SYNC_STATUS, APISyncStateTable.ApiSyncStates.SYNC_SUCCESS);
        values.put(_LAST_SYNC, lastSyncTime);
        values.put(_LAST_UPDATE, lastUpdateTime);
        return values;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void clearDirty() {
        isDirty = false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof ApiSyncState)) {
            return false;
        } else {
            return ((ApiSyncState) other).getTableName().equals(tableName);
        }
    }

    public void clearNew() {}

    public static class Factory {

        public static ApiSyncState newEntryFromCursor(Cursor cursor) {
            long time;
            String tableName = cursor.getString(cursor.getColumnIndex(_TABLE_NAME));

            ApiSyncState entry = new ApiSyncState(tableName);
            entry.id = cursor.getInt(cursor.getColumnIndex(APISyncStateTable.ApiSyncStates._ID));
            entry.lastUpdateTime = cursor.getLong(cursor.getColumnIndex(APISyncStateTable.ApiSyncStates._LAST_UPDATE));
            entry.lastSyncTime = cursor.getLong(cursor.getColumnIndex(_LAST_SYNC));
            entry.lastSyncStatus = cursor.getInt(cursor.getColumnIndex(_LAST_SYNC_STATUS));

            return entry;
        }
    }
}
