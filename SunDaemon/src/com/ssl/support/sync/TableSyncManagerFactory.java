package com.ssl.support.sync;

import android.content.Context;
import com.ssl.metadata.database.Table;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.data.models.ApiSyncState;

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
