package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract.BookListTags;

public class BookListTagTable extends AbstractTable {
	
	public static final String TABLE_NAME = "book_list_tag";
	
	public static final String[] ALL_COLUMNS = {
		BookListTags._ID,
		BookListTags._BOOK_LIST_ID,
		BookListTags._TAG_ID
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{BookListTags._ID, "INTEGER PRIMARY KEY"},
		{BookListTags._BOOK_LIST_ID, "INTEGER"},
		{BookListTags._TAG_ID, "INTEGER"}
	};
	
	public BookListTagTable(DBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
		// TODO Auto-generated constructor stub
	}

}
