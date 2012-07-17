package com.sunshine.metadata.provider;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.database.MetadataDBHandler.TableType;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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
		sUriMatcher.addURI(AUTHORITY, "packages", PACKAGES);
		sUriMatcher.addURI(AUTHORITY, "packages/#", PACKAGES_ID);
	}

	/*
	 * Constants for handling MIME_TYPE
	 */
	private static final String DIR_MIME_TYPE = "vnd.android.cursor.dir/vnd."
			+ AUTHORITY;
	private static final String ITEM_MIME_TYPE = "vnd.android.cursor.item/vnd."
			+ AUTHORITY;

	private static final String PACKAGES_MIME_TYPE = DIR_MIME_TYPE
			+ ".packages";
	private static final String PACKAGES_ID_MIME_TYPE = ITEM_MIME_TYPE
			+ ".packages";

	private MetadataDBHandler dbHandler;

	@Override
	public boolean onCreate() {
		dbHandler = new MetadataDBHandler(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (sUriMatcher.match(uri)) {
		case PACKAGES:
			return dbHandler.getTableManager(TableType.PACKAGE_TABLE).query(
					uri, projection, selection, selectionArgs, sortOrder);
		case PACKAGES_ID:
			return dbHandler.getTableManager(TableType.PACKAGE_TABLE).query(
					uri, projection, MetadataContract.Packages._ID + " = ?",
					new String[] { uri.getLastPathSegment() }, sortOrder);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
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
		switch (sUriMatcher.match(uri)) {
		case PACKAGES:
			return dbHandler.getTableManager(TableType.PACKAGE_TABLE).insert(
					uri, values);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (sUriMatcher.match(uri)) {
		case PACKAGES:
			return dbHandler.getTableManager(TableType.PACKAGE_TABLE).delete(
					uri, selection, selectionArgs);
		case PACKAGES_ID:
			return dbHandler.getTableManager(TableType.PACKAGE_TABLE).delete(
					uri, selection, selectionArgs);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		switch (sUriMatcher.match(uri)) {
		case PACKAGES:
			return dbHandler.getTableManager(TableType.PACKAGE_TABLE).update(
					uri, values, selection, selectionArgs);
		case PACKAGES_ID:
			return dbHandler.getTableManager(TableType.PACKAGE_TABLE).update(
					uri, values, MetadataContract.Packages._ID + "=?",
					new String[] { uri.getLastPathSegment() });
		default:
			throw new IllegalArgumentException();
		}
	}

}
