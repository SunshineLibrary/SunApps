package com.sunshine.metadata.database.tables;

import android.provider.BaseColumns;

import com.sunshine.metadata.database.MetadataDBHandler;

/**
 * @author Bowen Sun
 *
 */
public class APISyncStateTable extends Table {
	
	public static final String TABLE_NAME = "api_sync_states";
	
	public static final class APISyncState {
		public static final String _ID = BaseColumns._ID;
		public static final String _TABLE_NAME = "table_name";
		public static final String _LAST_UPDATE = "last_update";
	}
	
	public static final String[] ALL_COLUMNS = {
		APISyncState._ID,
		APISyncState._TABLE_NAME,
		APISyncState._LAST_UPDATE
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{APISyncState._ID, "INTEGER PRIMARY KEY"},
		{APISyncState._TABLE_NAME, "TEXT"},
		{APISyncState._LAST_UPDATE, "INTEGER"}
	};

	public APISyncStateTable(MetadataDBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
	}
}