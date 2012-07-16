package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.DatabaseTestCase;
import com.sunshine.metadata.database.tables.PackageTable;
import com.sunshine.metadata.provider.MetadataContract;

public class PackageTableTest extends DatabaseTestCase {

	public void testPackageTableCreation() {
		insertionTest(PackageTable.TABLE_NAME, new String[][] {
				{MetadataContract.Packages._NAME, "com.sunshine.daemon", "TEXT"},
				{MetadataContract.Packages._VERSION, "1", "INTEGER"},
		});
	}

}