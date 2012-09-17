package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract.Tags;

public class TagTable extends AbstractTable {

	public static final String TABLE_NAME = "tags";
	
	public static final String[] ALL_COLUMNS = {
		Tags._ID,
		Tags._NAME,
		Tags._TYPE,
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{Tags._ID, "INTEGER PRIMARY KEY"},
		{Tags._NAME, "TEXT"},
		{Tags._TYPE, "TEXT"},
	};
	
	public TagTable(DBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
		// TODO Auto-generated constructor stub
	}

}
