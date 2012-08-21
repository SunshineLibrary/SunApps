package com.sunshine.metadata.database;

import android.content.Context;
import com.sunshine.metadata.database.tables.PackageTable;

public class SystemDBHandlerFactory {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "system";

    public static DBHandler newSystemDBHandler(Context context) {
        DBHandler dbHandler = new DBHandler(context, DB_NAME, DB_VERSION);

        initTables(dbHandler);

        return dbHandler;
    }

    private static void initTables(DBHandler dbHandler) {
        dbHandler.addTableManager(PackageTable.TABLE_NAME, new PackageTable(dbHandler));
    }
}
