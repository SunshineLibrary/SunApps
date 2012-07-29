package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.BookCollections;

public class BookCollectionTable extends Table {

	public BookCollectionTable(MetadataDBHandler db) {
		super(db);
		// TODO Auto-generated constructor stub
	}
	
	public static final String TABLE_NAME = "book_collections";
	
	public static final String[] ALL_COLUMNS = {
		BookCollections._ID,
		BookCollections._NAME,
		BookCollections._AUTHOR,
		BookCollections._DESCRIPTION,
		BookCollections._PUBLISHER,
		BookCollections._COVER
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{BookCollections._ID, "INTEGER PRIMARY KEY"},
		{BookCollections._NAME, "TEXT"},
		{BookCollections._DESCRIPTION, "TEXT"},
		{BookCollections._AUTHOR, "TEXT"},
		{BookCollections._PUBLISHER, "TEXT"},
		{BookCollections._COVER,"TEXT"}
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
