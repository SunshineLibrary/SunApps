package com.sunshine.metadata.database;

import com.sunshine.metadata.database.tables.BookTable;
import com.sunshine.metadata.database.tables.PackageTable;
import com.sunshine.metadata.database.tables.Table;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Bowen Sun
 * 
 *         Manages the meta-data database,
 * 
 */
public class MetadataDBHandler extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "metadb";
	private Table tableManagers[] = new Table[2];
	
	public static enum TableType {
		PACKAGE_TABLE,
		BOOK_TABLE
	}

	/**
	 * @param context
	 */
	public MetadataDBHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		initTables();
	}
	
	private void initTables(){
		setTableManager(TableType.PACKAGE_TABLE, new PackageTable(this));
		setTableManager(TableType.BOOK_TABLE, new BookTable(this));
	}
	
	private void setTableManager(TableType tableType, Table tableManager) {
		tableManagers[tableType.ordinal()] = tableManager;
	}
	
	public Table getTableManager(TableType tableType){
		return tableManagers[tableType.ordinal()];
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (Table table: tableManagers) {
			table.createTable(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		for (Table table: tableManagers) {
			table.upgradeTable(db, oldVersion, newVersion);
		}

	}
}
