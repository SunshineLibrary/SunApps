package com.sunshine.support.data.daos;

import android.content.ContentValues;
import android.database.Cursor;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.tables.APISyncStateTable;
import com.sunshine.support.data.models.ApiSyncState;

import java.util.*;

/**
 * DAO for accessing Table Sync States
 *
 * Keeps track of update time, sync time and sync status. Persist results to database when done.
 *
 * @author Bowen Sun
 * @version 1.0
 */
public class ApiSyncStateDao extends AbstractPersistentDao<ApiSyncState> {

    private APISyncStateTable syncTable;

    private Map<String, ApiSyncState> syncStateMap;

    public ApiSyncStateDao(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
        syncTable = (APISyncStateTable) dbHandler.getTableManager(APISyncStateTable.TABLE_NAME);
        syncStateMap = new HashMap<String, ApiSyncState>();
        fetch();
    }

    public List<ApiSyncState> getAllSyncStates() {
        List<ApiSyncState> states = new ArrayList<ApiSyncState>();
        for (ApiSyncState state : getAllFetched()) {
            states.add(state);
        }
        return states;
    }

    public ApiSyncState getApiSyncStateForTable(String tableName) {
        if (syncStateMap.containsKey(tableName)) {
            return syncStateMap.get(tableName);
        } else {
            ApiSyncState state = new ApiSyncState(tableName);
            syncStateMap.put(tableName, state);
            return state;
        }
    }

    @Override
    public void fetch() {
        Cursor cursor = syncTable.query(null, APISyncStateTable.ALL_COLUMNS, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ApiSyncState state = ApiSyncState.Factory.newEntryFromCursor(cursor);
                syncStateMap.put(state.getTableName(), state);
            } while (cursor.moveToNext());
        }
    }

    @Override
    protected Collection<ApiSyncState> getAllFetched() {
        return syncStateMap.values();
    }

    @Override
    protected void update(int id, ContentValues values) {
        syncTable.update(null, values, APISyncStateTable.APISyncState._ID + "=?", new String[] {String.valueOf(id)});
    }

    @Override
    protected void insert(ContentValues values) {
        syncTable.insert(null, values);
    }
}
