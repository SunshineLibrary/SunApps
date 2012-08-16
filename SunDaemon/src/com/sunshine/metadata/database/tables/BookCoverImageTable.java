package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.provider.MetadataContract.BookCoverImages;

public class BookCoverImageTable extends DownloadableTable {
	 public static final String TABLE_NAME = "book_cover_images";
	
	 private static final String[] ALL_COLUMNS = {
		 BookCoverImages._ID,
		 BookCoverImages._BOOK_ID
	 };
	 
	 private static final String[][] COLUMN_DEFINITIONS = {
		 {BookCoverImages._ID, "INTEGER PRIMARY KEY"},
		 {BookCoverImages._BOOK_ID, "TEXT"}
	 };
	
	public BookCoverImageTable(DBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
	}

}
