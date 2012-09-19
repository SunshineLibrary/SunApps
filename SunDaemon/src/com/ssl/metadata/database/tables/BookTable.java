package com.ssl.metadata.database.tables;

import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract.Books;

/**
 * @author Bowen Sun
 *
 */
public class BookTable extends DownloadableTable {
	
	public static final String TABLE_NAME = "books";
	
	public static final String[] ALL_COLUMNS = {
		Books._ID,
		Books._TITLE,
		Books._INTRO,
		Books._AUTHOR,
		Books._AUTHOR_ID,
		Books._COLLECTION_ID,
		Books._PUBLISHER,
		Books._PUBLICATION_YEAR,
		Books._PROGRESS,
		Books._STARTTIME
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{Books._ID, "INTEGER PRIMARY KEY"},
		{Books._TITLE, "TEXT"},
		{Books._INTRO, "TEXT"},
		{Books._AUTHOR, "TEXT"},
		{Books._AUTHOR_ID, "INTEGER"},
		{Books._COLLECTION_ID, "INTEGER"},
		{Books._PUBLISHER, "TEXT"},
		{Books._PUBLICATION_YEAR, "TEXT"},
		{Books._PROGRESS, "INTEGER"},
		{Books._STARTTIME, "DATETIME"}
	};

	public BookTable(DBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
	}
}
