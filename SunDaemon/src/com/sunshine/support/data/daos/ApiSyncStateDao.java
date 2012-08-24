package com.sunshine.support.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.tables.APISyncStateTable;
import com.sunshine.support.application.MessageApplication;
import com.sunshine.support.application.UIMessage;
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

    public static final String ON_NEW_STATE = "ApiSyncStateTable.ON_NEW_CHANGE";

    public static final String ON_STATE_CHANGE = "ApiSyncStateTable.ON_STATE_CHANGE";

    private Context context;
    private APISyncStateTable syncTable;
    private Map<String, ApiSyncState> syncStateMap;

    public ApiSyncStateDao(Context context, DBHandler dbHandler) {
        this.context = context;
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
        try {
            if (cursor.moveToFirst()) {
                do {
                    ApiSyncState state = ApiSyncState.Factory.newEntryFromCursor(cursor);
                    syncStateMap.put(state.getTableName(), state);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    protected Collection<ApiSyncState> getAllFetched() {
        return syncStateMap.values();
    }

    @Override
    protected void update(ApiSyncState state, ContentValues values) {
        syncTable.update(null, values, APISyncStateTable.ApiSyncStates._ID + "=?",
                new String[] {String.valueOf(state.getId())});
        ((MessageApplication) context.getApplicationContext())
                .postMessage(new UIMessage<ApiSyncState>(ON_STATE_CHANGE, state));
    }

    @Override
    protected void insert(ApiSyncState state, ContentValues values) {
        long id =  dbHandler.getWritableDatabase().insert(syncTable.getTableName(), null, values);
        state.setId(id);
        ((MessageApplication) context.getApplicationContext())
                .postMessage(new UIMessage<ApiSyncState>(ON_NEW_STATE, state));
    }
}
