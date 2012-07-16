package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.Books;

/**
 * @author Bowen Sun
 *
 */
public class BookTable extends Table {
	
	public static final String TABLE_NAME = "books";
	
	public static final String[] ALL_COLUMNS = {
		Books._ID,
		Books._TITLE,
		Books._DESCRIPTION,
		Books._AUTHOR,
		Books._PROGRESS
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{Books._ID, "INTEGER PRIMARY KEY"},
		{Books._TITLE, "TEXT"},
		{Books._DESCRIPTION, "TEXT"},
		{Books._AUTHOR, "TEXT"},
		{Books._PROGRESS, "INTEGER"}
	};

	public BookTable(MetadataDBHandler db) {
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
