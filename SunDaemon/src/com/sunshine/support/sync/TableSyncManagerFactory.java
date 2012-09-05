package com.sunshine.support.sync;

import android.content.Context;
import com.sunshine.metadata.database.Table;
import com.sunshine.support.api.ApiClientFactory;
import com.sunshine.support.data.models.ApiSyncState;

/**
 * Creates TableSyncManagers for APISyncService
 *
 * @author Bowen Sun
 * @version 1.0
 */
public class TableSyncManagerFactory {

    public static TableSyncManager getManager(Context context, Table table, ApiSyncState syncState) {
        return new BasicTableSyncManager(table, syncState, ApiClientFactory.newApiClient(context));
    }
}
