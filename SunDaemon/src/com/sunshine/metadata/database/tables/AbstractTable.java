package com.sunshine.metadata.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.database.Table;

/**
 * @author Bowen Sun
 * 
 *         TableManager provides an abstraction over the meta-data database.
 *         Each table should have a corresponding TableManager to create and
 *         upgrade the tables accordingly.
 * 
 */

public abstract class AbstractTable implements Table {

	protected MetadataDBHandler dbHandler;
    private String tableName;
    private String[][] columnDefinitions;
    private String[] columns;

    /**
	 * TableManager requires a handle on the DatabaseHandler in order to access
	 * the database.
	 * 
	 * @param handler
	 */
	public AbstractTable(MetadataDBHandler handler, String tableName, String[][] columnDefinitions, String[] columns) {
		this.dbHandler = handler;
        this.tableName = tableName;
        this.columnDefinitions = columnDefinitions;
        this.columns = columns;
	}

	/**
	 * @return name of the table to be managed
	 */
	public String getTableName() {
       return tableName;
    }

	/**
	 * @return an array of column name and column definition pairs
	 */
	public String[][] getColumnDefinitions() {
        return columnDefinitions;
    }
	
	/**
	 * @return an array of all column names
	 */
	public String[] getColumns() {
        return columns;
    }

	/**
	 * Create the database to be managed. Calls getColumnDefinitions() to
	 * construct the table with a default query.
	 * 
	 * @param db
	 */
	public void createTable(SQLiteDatabase db) {
		db.execSQL(this.getCreateQuery());
	}

	protected String getCreateQuery() {
		String query = "CREATE TABLE " + this.getTableName() + "(";
		for (String[] pair : getColumnDefinitions()) {
			query += pair[0] + " " + pair[1] + " , ";
		}
		query = query.substring(0, query.lastIndexOf(" , ")) + ");";
		return query;
	}

	/**
	 * Upgrade the database accordingly, by default destroy the old table and
	 * create a new one.
	 */
	public void upgradeTable(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.dropTable(db);
		this.createTable(db);
	}

	private void dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE " + this.getTableName() + ";");
	}

	/*
	 * Proxy methods for the content provider to individual tables in the
	 * database.
	 */
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return dbHandler.getWritableDatabase().query(getTableName(),
				projection, selection, selectionArgs, null, null, sortOrder);
	}

	public Uri insert(Uri uri, ContentValues values) {
		long id = dbHandler.getWritableDatabase().insert(getTableName(), null, values);
		if (uri != null) {
			return uri.buildUpon().appendPath("" + id).build();
		} else {
			return null;
		}
	}

	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return dbHandler.getWritableDatabase().delete(getTableName(), selection, selectionArgs);
	}

	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return dbHandler.getWritableDatabase().update(getTableName(), values, selection, selectionArgs);
	}
}
