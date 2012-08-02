package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.BookTags;

public class BookTagTable extends AbstractTable {
	
	public static final String TABLE_NAME = "book_tag";
	
	public static final String[] ALL_COLUMNS = {
		BookTags._ID,
		BookTags._BOOK_ID,
		BookTags._TAG_ID
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{BookTags._ID, "INTEGER PRIMARY KEY"},
		{BookTags._BOOK_ID, "INTEGER"},
		{BookTags._TAG_ID, "INTEGER"}
	};
	
	public BookTagTable(MetadataDBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
	}
}
