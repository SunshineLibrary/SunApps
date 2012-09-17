package com.ssl.support.data.models;

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
