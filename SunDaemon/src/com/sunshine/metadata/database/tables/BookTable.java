package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.Books;

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
		Books._COLLECTION_ID,
		Books._PUBLISHER,
		Books._PUBLICATION_YEAR,
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{Books._ID, "INTEGER PRIMARY KEY"},
		{Books._TITLE, "TEXT"},
		{Books._INTRO, "TEXT"},
		{Books._AUTHOR, "TEXT"},
		{Books._COLLECTION_ID, "TEXT"},
		{Books._PUBLISHER, "TEXT"},
		{Books._PUBLICATION_YEAR, "TEXT"},
	};

	public BookTable(MetadataDBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
	}
}
