package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.BookTag;

public class BookTagTable extends Table {
	
	public static final String TABLE_NAME = "book_tag";
	
	public static final String[] ALL_COLUMNS = {
		BookTag._ID,
		BookTag._BOOKID,
		BookTag._TAGID
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{BookTag._ID, "INTEGER PRIMARY KEY"},
		{BookTag._BOOKID, "INTEGER"},
		{BookTag._TAGID, "INTEGER"}
	};
	
	public BookTagTable(MetadataDBHandler db) {
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

	@Override
	public String[] getColumns() {
		return ALL_COLUMNS;
	}

}
