package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.provider.MetadataContract.UserBook;

public class UserBookTable extends AbstractTable {

	public static final String TABLE_NAME = "user_book";
	
	public static final String[] ALL_COLUMNS = {
		UserBook._ID,
		UserBook._USER_ID,
		UserBook._BOOK_ID,
		UserBook._PROGRESS,
		UserBook._FIRST_READ_AT,
		UserBook._LAST_READ_AT,
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{UserBook._ID, "INTEGER PRIMART KEY"},
		{UserBook._USER_ID, "TEXT"},
		{UserBook._BOOK_ID, "TEXT"},
		{UserBook._PROGRESS, "TEXT"},
		{UserBook._FIRST_READ_AT, "TEXT"},
		{UserBook._LAST_READ_AT, "TEXT"},
	};
	
	public UserBookTable(DBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
		// TODO Auto-generated constructor stub
	}

}
