package com.sunshine.metadata.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * @author Bowen Sun
 *
 * Manages the meta-data database,
 */
public class MetadataDBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "metadb";
    private HashMap<String, Table> tableManagers;
    private HashMap<String, TableView> tableViewManagers;

    /**
     * @param context
     */
    public MetadataDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        tableManagers = new HashMap<String, Table>();
        tableViewManagers = new HashMap<String, TableView>();
    }

    public void addTableManager(String tableName, Table table) {
        tableManagers.put(tableName, table);
    }

    public Table getTableManager(String tableName) {
        return tableManagers.get(tableName);
    }

    public void addTableViewManager(String viewName, TableView view) {
        tableViewManagers.put(viewName, view);
    }

    public TableView getTableViewManager(String viewName) {
        return tableViewManagers.get(viewName);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Table table : tableManagers.values()) {
            table.createTable(db);
        }
        for (TableView view : tableViewManagers.values()) {
            view.createView(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Table table : tableManagers.values()) {
            table.upgradeTable(db, oldVersion, newVersion);
        }
    }
}
