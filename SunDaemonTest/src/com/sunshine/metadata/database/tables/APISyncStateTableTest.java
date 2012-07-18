package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.DatabaseTestCase;

public class APISyncStateTableTest extends DatabaseTestCase {
	
	public void testApiTableCreation() {
		insertionTest(APISyncStateTable.TABLE_NAME, new String[][] {
				{APISyncStateTable.APISyncState._TABLE_NAME, "book", "TEXT"},
				{APISyncStateTable.APISyncState._LAST_UPDATE, "1234567", "INTEGER"}
		});
	}

}