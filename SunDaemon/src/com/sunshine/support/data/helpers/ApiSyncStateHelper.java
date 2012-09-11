package com.sunshine.support.data.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.sunshine.metadata.database.tables.APISyncStateTable;
import com.sunshine.support.data.models.ApiSyncState;

import static com.sunshine.metadata.database.tables.APISyncStateTable.ApiSyncStates;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class ApiSyncStateHelper {

    public static ApiSyncState getOrCreateNewFromTableName(Context context, String tableName) {
        Cursor cursor = context.getContentResolver().query(ApiSyncStates.CONTENT_URI,
                APISyncStateTable.ALL_COLUMNS, ApiSyncStates._TABLE_NAME + "=?",
                new String[]{tableName}, null);
        if (cursor.moveToFirst()) {
            return newEntryFromCursor(cursor);
        } else {
            return createNewFromTableName(context, tableName);
        }
    }

    public static void saveApiSyncState(Context context, ApiSyncState syncState) {
        context.getContentResolver().update(ApiSyncStates.CONTENT_URI, getUpdateContentValues(syncState),
                ApiSyncStates._ID + "=" + syncState.id, null);
        context.getContentResolver().notifyChange(ApiSyncStates.CONTENT_URI, null);
    }

    public static ApiSyncState newEntryFromCursor(Cursor cursor) {
        String tableName = cursor.getString(cursor.getColumnIndex(ApiSyncStates._TABLE_NAME));
        long id = cursor.getInt(cursor.getColumnIndex(APISyncStateTable.ApiSyncStates._ID));

        ApiSyncState entry = new ApiSyncState(id, tableName);
        entry.lastUpdateTime = cursor.getLong(cursor.getColumnIndex(APISyncStateTable.ApiSyncStates._LAST_UPDATE));
        entry.lastSyncTime = cursor.getLong(cursor.getColumnIndex(ApiSyncStates._LAST_SYNC));
        entry.lastSyncStatus = cursor.getInt(cursor.getColumnIndex(ApiSyncStates._LAST_SYNC_STATUS));

        return entry;
    }

    private static ApiSyncState createNewFromTableName(Context context, String tableName) {
        Uri uri = context.getContentResolver().insert(ApiSyncStates.CONTENT_URI, getNewEntryContentValues(tableName));
        return new ApiSyncState(Long.parseLong(uri.getLastPathSegment()), tableName);
    }

    private static ContentValues getNewEntryContentValues(String tableName) {
        ContentValues values = new ContentValues();
        values.put(ApiSyncStates._TABLE_NAME, tableName);
        return values;
    }

    private static ContentValues getUpdateContentValues(ApiSyncState syncState) {
        ContentValues values = new ContentValues();
        values.put(ApiSyncStates._LAST_SYNC, syncState.lastSyncTime);
        values.put(ApiSyncStates._LAST_UPDATE, syncState.lastUpdateTime);
        values.put(ApiSyncStates._LAST_SYNC_STATUS, syncState.lastSyncStatus);
        return values;
    }
}
