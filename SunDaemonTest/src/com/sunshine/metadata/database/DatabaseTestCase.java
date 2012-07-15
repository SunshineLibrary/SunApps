package com.sunshine.metadata.database;

import com.sunshine.metadata.database.MetadataDBHandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.mock.MockContext;
import junit.framework.TestCase;

public class DatabaseTestCase extends TestCase {
	
	public static MetadataDBHandler dbHandler;
	public static SQLiteDatabase db;
	
	public void setUp(){
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

}
