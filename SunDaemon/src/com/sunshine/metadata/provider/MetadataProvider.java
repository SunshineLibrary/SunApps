package com.sunshine.metadata.provider;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.database.tables.PackageTable;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MetadataProvider extends ContentProvider {

	/*
	 * Defining constants for matching the content URI
	 */
	private static final String AUTHORITY = MetadataContract.AUTHORITY;

	private static final UriMatcher sUriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	private static final int PACKAGES = 1;
	private static final int PACKAGES_ID = 2;

	static {
		sUriMatcher.addURI(AUTHORITY, "/packages", PACKAGES);
		sUriMatcher.addURI(AUTHORITY, "/packages/#", PACKAGES_ID);
	}

	/*
	 * Constants for handling MIME_TYPE
	 */
	private static final String DIR_MIME_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY;
	private static final String ITEM_MIME_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY;
	
	private static final String PACKAGES_MIME_TYPE = DIR_MIME_TYPE + ".packages";
	private static final String PACKAGES_ID_MIME_TYPE = ITEM_MIME_TYPE + ".packages";



	private MetadataDBHandler dbHandler;

	private SQLiteDatabase db;

	@Override
	public boolean onCreate() {
		dbHandler = new MetadataDBHandler(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		//TODO: query need to be made thread-safe
		db = dbHandler.getReadableDatabase();
		
		String tableName, groupBy, having;
		groupBy = having = null;
		
		switch (sUriMatcher.match(uri)) {
		case PACKAGES:
			tableName = PackageTable.TABLE_NAME;
			break;
		case PACKAGES_ID:
			selection = selection + " " + MetadataContract.Packages._ID + " = " + uri.getLastPathSegment();
			tableName = PackageTable.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException();
		}
		
		return db.query(tableName, projection, selection, selectionArgs, groupBy, having, sortOrder);
	}

	@Override
	public String getType(Uri uri) {
		switch(sUriMatcher.match(uri)){
		case PACKAGES:
			return PACKAGES_MIME_TYPE;
		case PACKAGES_ID:
			return PACKAGES_ID_MIME_TYPE;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) { 
		long id;
		SQLiteDatabase db = dbHandler.getWritableDatabase();
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
