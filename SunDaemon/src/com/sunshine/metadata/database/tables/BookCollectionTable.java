package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.provider.MetadataContract.BookCollections;

public class BookCollectionTable extends DownloadableTable {

	public static final String TABLE_NAME = "book_collections";
	
	public static final String[] ALL_COLUMNS = {
		BookCollections._ID,
		BookCollections._TITLE,
		BookCollections._AUTHOR,
//		BookCollections._AUTHOR_ID,
		BookCollections._INTRO,
		BookCollections._PUBLISHER,
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{BookCollections._ID, "INTEGER PRIMARY KEY"},
		{BookCollections._TITLE, "TEXT"},
		{BookCollections._INTRO, "TEXT"},
		{BookCollections._AUTHOR, "TEXT"},
//		{BookCollections._AUTHOR_ID, "INTEGER"},
		{BookCollections._PUBLISHER, "TEXT"},
	};

    public BookCollectionTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
