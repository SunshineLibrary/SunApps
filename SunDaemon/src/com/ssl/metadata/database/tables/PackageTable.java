package com.ssl.metadata.database.tables;

import android.database.sqlite.SQLiteDatabase;
import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract.Packages;

/**
 * @author Bowen Sun
 *
 */
public class PackageTable extends DownloadableTable {
	
	public static final String TABLE_NAME = "packages";
	
	public static final String[] ALL_COLUMNS = {
		Packages._ID,
		Packages._NAME,
		Packages._VERSION,
        Packages._INSTALL_STATUS,
	};
	
	public static final String[][] COLUMN_DEFINITIONS = {
		{Packages._ID, "INTEGER PRIMARY KEY"},
		{Packages._NAME, "TEXT"},
		{Packages._VERSION, "INTEGER"},
        {Packages._INSTALL_STATUS, "INTEGER"}
	};

	public PackageTable(DBHandler db) {
		super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
	}

    @Override
    public void upgradeTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do not drop this table
    }
}
