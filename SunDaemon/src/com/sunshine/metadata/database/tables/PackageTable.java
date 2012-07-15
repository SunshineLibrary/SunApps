package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.Packages;

/**
 * @author Bowen Sun
 *
 */
public class PackageTable extends Table {
	
	public static final String TABLE_NAME = "packages";
	
	public static final String[] ALL_COLUMNS = {
		Packages._ID,
		Packages._NAME,
		Packages._VERSION
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{Packages._ID, "INTEGER PRIMARY KEY"},
		{Packages._NAME, "TEXT"},
		{Packages._VERSION, "INTEGER"}
	};

	public PackageTable(MetadataDBHandler db) {
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
