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
		public static final String _API_PATH = "api_path";
		public static final String _LAST_UPDATE = "last_update";
	}
	
	public static final String[] ALL_COLUMNS = {
		APISyncState._ID,
		APISyncState._API_PATH,
		APISyncState._LAST_UPDATE
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{APISyncState._ID, "INTEGER PRIMARY KEY"},
		{APISyncState._API_PATH, "TEXT"},
		{APISyncState._LAST_UPDATE, "INTEGER"}
	};

	public APISyncStateTable(MetadataDBHandler db) {
		super(db);
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public String[][] getColumnDefinitions() {
		return COLUMN_DEFINITIONS;
	}
}