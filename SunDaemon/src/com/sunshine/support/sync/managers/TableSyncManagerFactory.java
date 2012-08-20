package com.sunshine.support.sync.managers;

import com.sunshine.metadata.database.Table;

/**
 * Creates TableSyncManagers for APISyncService
 *
 * @author Bowen Sun
 * @version 1.0
 */
public class TableSyncManagerFactory {

    public static TableSyncManager getManager(Table table, Table syncTable) {
        return new BasicTableSyncManager(table, syncTable);
    }
}
