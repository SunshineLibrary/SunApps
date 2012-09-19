package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract.BookCollectionTags;

public class BookCollectionTagTable extends AbstractTable {
	
	public static final String TABLE_NAME = "book_collections_tags";
	
	public static final String[] ALL_COLUMNS = {
		BookCollectionTags._ID,
		BookCollectionTags._BOOK_COLLECTION_ID,
		BookCollectionTags._TAG_ID
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{BookCollectionTags._ID, "INTEGER PRIMARY KEY"},
		{BookCollectionTags._BOOK_COLLECTION_ID, "INTEGER"},
		{BookCollectionTags._TAG_ID, "INTEGER"}
	};
	
	public BookCollectionTagTable(DBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
		// TODO Auto-generated constructor stub
	}

}
