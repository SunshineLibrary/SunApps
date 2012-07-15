package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author Bowen Sun
 * 
 *         TableManager provides an abstraction over the meta-data database.
 *         Each table should have a corresponding TableManager to create and
 *         upgrade the tables accordingly.
 * 
 */

public abstract class Table {

	protected MetadataDBHandler dbHandler;

	/**
	 * TableManager requires a handle on the DatabaseHandler in order to access
	 * the database.
	 * 
	 * @param handler
	 */
	public Table(MetadataDBHandler handler) {
		this.dbHandler = handler;
	}

	/**
	 * @return name of the table to be managed
	 */
	public abstract String getTableName();

	/**
	 * @return an array of column name and column definition pairs
	 */
	public abstract String[][] getColumnDefinitions();

	/**
	 * Create the database to be managed. Calls getColumnDefinitions() to
	 * construct the table with a default query.
	 * 
	 * @param db
	 */
	public void createTable(SQLiteDatabase db) {
		db.execSQL(this.getCreateQuery());
	}

	private String getCreateQuery() {
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
}
