package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.BookLists;

public class BookListTable extends Table {

	public BookListTable(MetadataDBHandler db) {
		super(db);
		// TODO Auto-generated constructor stub
	}
	
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
	
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] getColumnDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getColumns() {
		// TODO Auto-generated method stub
		return null;
	}

}
