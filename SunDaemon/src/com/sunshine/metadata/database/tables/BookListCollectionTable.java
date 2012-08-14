package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.provider.MetadataContract.BookListCollections;

public class BookListCollectionTable extends AbstractTable {

public static final String TABLE_NAME = "book_list_collection";
	
	public static final String[] ALL_COLUMNS = {
		BookListCollections._ID,
		BookListCollections._BOOK_LIST_ID,
		BookListCollections._BOOK_COLLECTION_ID
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{BookListCollections._ID, "INTEGER PRIMARY KEY"},
		{BookListCollections._BOOK_LIST_ID, "INTEGER"},
		{BookListCollections._BOOK_COLLECTION_ID, "INTEGER"}
	};
	
	public BookListCollectionTable(DBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
	}

}
