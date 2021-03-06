package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract.BookLists;

public class BookListTable extends DownloadableTable {

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

    public BookListTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
