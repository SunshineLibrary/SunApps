package com.sunshine.support.metadata.database.tables;

import android.content.ContentValues;
import android.database.Cursor;

import com.sunshine.metadata.database.DatabaseTestCase;
import com.sunshine.metadata.database.tables.PackageTable;
import com.sunshine.metadata.provider.MetadataContract;

public class PackageTableTest extends DatabaseTestCase {

	public void testPackageTableCreation() {
		String pkgName = "com.sunshine.support.daemon";
		int version = 1;
		ContentValues values = new ContentValues();
		values.put(MetadataContract.Packages._NAME, pkgName);
		values.put(MetadataContract.Packages._VERSION, version);
		db.insert(PackageTable.TABLE_NAME, null, values);
		
		Cursor cur = db.query(PackageTable.TABLE_NAME, PackageTable.ALL_COLUMNS,
				MetadataContract.Packages._NAME + " = ? ", new String[]{pkgName}, null, null, null);
		
		assertTrue(cur.moveToFirst());
		
		int index = cur.getColumnIndex(MetadataContract.Packages._ID);
		assertTrue(cur.getInt(index) == 1);
		
		index = cur.getColumnIndex(MetadataContract.Packages._NAME);
		assertTrue(cur.getString(index).equals(pkgName));
		
		index = cur.getColumnIndex(MetadataContract.Packages._VERSION);
		assertTrue(cur.getInt(index) == version);
	}

}