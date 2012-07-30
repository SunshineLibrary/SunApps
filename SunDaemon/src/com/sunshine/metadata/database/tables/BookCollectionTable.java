package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.AbstractTable;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.BookCollections;

public class BookCollectionTable extends AbstractTable {

	public static final String TABLE_NAME = "book_collections";
	
	public static final String[] ALL_COLUMNS = {
		BookCollections._ID,
		BookCollections._NAME,
		BookCollections._AUTHOR,
		BookCollections._DESCRIPTION,
		BookCollections._PUBLISHER,
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{BookCollections._ID, "INTEGER PRIMARY KEY"},
		{BookCollections._NAME, "TEXT"},
		{BookCollections._DESCRIPTION, "TEXT"},
		{BookCollections._AUTHOR, "TEXT"},
		{BookCollections._PUBLISHER, "TEXT"},
	};

    public BookCollectionTable(MetadataDBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
