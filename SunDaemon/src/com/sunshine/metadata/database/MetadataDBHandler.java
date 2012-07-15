package com.sunshine.metadata.database;

import java.util.LinkedList;
import java.util.List;

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
	private static List<Table> tableManagers;

	/**
	 * @param context
	 */
	public MetadataDBHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		initTableManagers();
	}
	
	private void initTableManagers(){
		tableManagers = new LinkedList<Table>();
		tableManagers.add(new PackageTable(this));
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
