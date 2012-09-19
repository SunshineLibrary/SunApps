package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract.Authors;

public class AuthorTable extends DownloadableTable {

	public AuthorTable(DBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
	}
	
	public static final String TABLE_NAME = "authors";

    private static final String[] ALL_COLUMNS = {
    	Authors._ID,
    	Authors._NAME,
    	Authors._INTRO
    };

    private static final String[][] COLUMN_DEFINITIONS = {
    	{Authors._ID, "INTEGER PRIMARY KEY"},
        {Authors._NAME, "TEXT"},
        {Authors._INTRO, "TEXT"}
    };
}
