package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.AbstractTable;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.Packages;

/**
 * @author Bowen Sun
 *
 */
public class PackageTable extends AbstractTable {
	
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
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
	}
}
