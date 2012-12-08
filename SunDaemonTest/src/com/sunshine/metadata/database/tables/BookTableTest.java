package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.DatabaseTestCase;
import com.sunshine.metadata.provider.MetadataContract;

public class BookTableTest extends DatabaseTestCase {
	public void testPackageTableCreation() {
		insertionTest(BookTable.TABLE_NAME, new String[][] {
				{MetadataContract.Books._AUTHOR, "author", "TEXT"},
				{MetadataContract.Books._INTRO, "description", "TEXT"},
				{MetadataContract.Books._TITLE, "title", "TEXT"},
				{MetadataContract.Books._PROGRESS, "100", "INTEGER"}
		});
	}
}
