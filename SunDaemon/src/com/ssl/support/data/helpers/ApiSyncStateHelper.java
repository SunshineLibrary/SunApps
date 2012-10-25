package com.ssl.support.data.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.ssl.metadata.database.tables.APISyncStateTable;
import com.ssl.support.data.models.ApiSyncState;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class ApiSyncStateHelper {

    public static ApiSyncState getOrCreateNewFromTableName(Context context, String tableName) {
        ApiSyncState entry = null;

        Cursor cursor = context.getContentResolver().query(APISyncStateTable.ApiSyncStates.CONTENT_URI,
                APISyncStateTable.ALL_COLUMNS, APISyncStateTable.ApiSyncStates._TABLE_NAME + "=?",
                new String[]{tableName}, null);

        if (cursor.moveToFirst()) {
            entry = newEntryFromCursor(cursor);
        } else {
            entry = createNewFromTableName(context, tableName);
        }
        cursor.close();

        return entry;
    }

    public static void saveApiSyncState(Context context, ApiSyncState syncState) {
        context.getContentResolver().update(APISyncStateTable.ApiSyncStates.CONTENT_URI, getUpdateContentValues(syncState),
                APISyncStateTable.ApiSyncStates._ID + "=" + syncState.id, null);
        context.getContentResolver().notifyChange(APISyncStateTable.ApiSyncStates.CONTENT_URI, null);
    }

    public static ApiSyncState newEntryFromCursor(Cursor cursor) {
        String tableName = cursor.getString(cursor.getColumnIndex(APISyncStateTable.ApiSyncStates._TABLE_NAME));
        long id = cursor.getInt(cursor.getColumnIndex(APISyncStateTable.ApiSyncStates._ID));

        ApiSyncState entry = new ApiSyncState(id, tableName);
        entry.lastUpdateTime = cursor.getLong(cursor.getColumnIndex(APISyncStateTable.ApiSyncStates._LAST_UPDATE));
        entry.lastSyncTime = cursor.getLong(cursor.getColumnIndex(APISyncStateTable.ApiSyncStates._LAST_SYNC));
        entry.lastSyncStatus = cursor.getInt(cursor.getColumnIndex(APISyncStateTable.ApiSyncStates._LAST_SYNC_STATUS));

        return entry;
    }

    private static ApiSyncState createNewFromTableName(Context context, String tableName) {
        Uri uri = context.getContentResolver().insert(APISyncStateTable.ApiSyncStates.CONTENT_URI, getNewEntryContentValues(tableName));
        return new ApiSyncState(Long.parseLong(uri.getLastPathSegment()), tableName);
    }

    private static ContentValues getNewEntryContentValues(String tableName) {
        ContentValues values = new ContentValues();
        values.put(APISyncStateTable.ApiSyncStates._TABLE_NAME, tableName);
        return values;
    }

    private static ContentValues getUpdateContentValues(ApiSyncState syncState) {
        ContentValues values = new ContentValues();
        values.put(APISyncStateTable.ApiSyncStates._LAST_SYNC, syncState.lastSyncTime);
        values.put(APISyncStateTable.ApiSyncStates._LAST_UPDATE, syncState.lastUpdateTime);
        values.put(APISyncStateTable.ApiSyncStates._LAST_SYNC_STATUS, syncState.lastSyncStatus);
        return values;
    }
}
