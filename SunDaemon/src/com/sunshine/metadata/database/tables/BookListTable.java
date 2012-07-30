package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.BookLists;

public class BookListTable extends Table {

	public static final String TABLE_NAME = "book_lists";
	
	public static final String[] ALL_COLUMNS = {
		BookLists._ID,
		BookLists._NAME,
		BookLists._INTRO,
		BookLists._AUTHOR
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{BookLists._ID, "INTEGER PRIMARY KEY"},
		{BookLists._NAME, "TEXT"},
		{BookLists._INTRO, "TEXT"},
		{BookLists._AUTHOR, "TEXT"}
	};

    public BookListTable(MetadataDBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
