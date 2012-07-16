package com.sunshine.metadata.database;

import com.sunshine.metadata.database.MetadataDBHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.test.mock.MockContext;
import junit.framework.TestCase;

public class DatabaseTestCase extends TestCase {

	public static MetadataDBHandler dbHandler;
	public static SQLiteDatabase db;
	

	public void setUp() {
		Context context = new MockContext() {
			public SQLiteDatabase openOrCreateDatabase(String file, int mode,
					SQLiteDatabase.CursorFactory factory) {
				return SQLiteDatabase.create(null);
			}
		};
		dbHandler = new MetadataDBHandler(context);
		db = dbHandler.getWritableDatabase();
	}

	public void tearDown() {
		db.close();
	}

	protected void insertionTest(String tableName, String[][] valuePairs) {
		ContentValues values = new ContentValues();
		String[] columns = new String[valuePairs.length];

		for (int i = 0; i < valuePairs.length; i++) {
			String[] pair = valuePairs[i];
			values.put(pair[0], pair[1]);
			columns[i] = pair[0];
		}
		
		assertEquals(1, db.insert(tableName, null, values));

		Cursor cur = db.query(tableName, columns, BaseColumns._ID + " = 1", null, null, null, null);
		assertTrue(cur.moveToFirst());

		int index;
		for (String[] pair : valuePairs) {
			index = cur.getColumnIndex(pair[0]);
			if(pair[2].equals("INTEGER")){
				assertEquals(cur.getInt(index), Integer.parseInt(pair[1]));
			} else if (pair[2].equals("TEXT")) {
				assertEquals(cur.getString(index), pair[1]);
			} else {
				assertTrue(false);
			}
		}
	}

}
