package com.sunshine.metadata.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

public abstract class SunProviderTestCase extends
		ProviderTestCase2<MetadataProvider> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SunProviderTestCase(Class providerClass, String providerAuthority) {
		super(providerClass, providerAuthority);
	}

	public void completeSingleDirTest(Uri dirUri, String[][] valuePairs) {
		Uri itemUri = insertionTest(dirUri, valuePairs);
		updateTest(dirUri, itemUri, valuePairs);
		queryTest(dirUri, itemUri, valuePairs);
		deleteTest(itemUri);
	}

	private void queryTest(Uri dirUri, Uri itemUri, String[][] valuePairs) {
		String[] projection = new String[valuePairs.length];
		String[] selectionArgs = new String[valuePairs.length];
		String selection = "";
	
		for (int i = 0; i < valuePairs.length; i++) {
			projection[i] = valuePairs[i][0];
			selection += projection[i] + " = ? ";
			selectionArgs[i] = valuePairs[i][1];
			if ( i < valuePairs.length - 1) {
				selection += " AND ";
			}
		}
	
		Cursor cursor = getContentResolver().query(dirUri, projection,
				selection, selectionArgs, null);
		assertTrue(cursor.moveToFirst());
	
		cursor = getContentResolver().query(itemUri, projection, null, null,
				null);
		assertTrue(cursor.moveToFirst());
	
		for (String[] pair : valuePairs) {
			int index = cursor.getColumnIndex(pair[0]);
			assertEquals(pair[1], cursor.getString(index));
		}
	}

	private Uri insertionTest(Uri uri, String[][] valuePairs) {
		ContentValues values = new ContentValues();
	
		for (int i = 0; i < valuePairs.length; i++) {
			String[] pair = valuePairs[i];
			values.put(pair[0], pair[1]);
		}
	
		Uri insertedItem = getContentResolver().insert(uri, values);
		assertNotNull(insertedItem);
		return insertedItem;
	}

	private void updateTest(Uri dirUri, Uri itemUri, String[][] valuePairs) {
		ContentValues values = new ContentValues();
		ContentValues tempValues = new ContentValues();
	
		for (int i = 0; i < valuePairs.length; i++) {
			String[] pair = valuePairs[i];
			values.put(pair[0], pair[1]);
			tempValues.put(pair[0], pair[1] + pair[1]);
		}
	
		assertEquals(1, getContentResolver().update(dirUri, tempValues,
				valuePairs[0][0] + " = ?", new String[] { valuePairs[0][1] }));
	
		assertEquals(1, getContentResolver()
				.update(itemUri, values, null, null));
	}

	private void deleteTest(Uri itemUri) {
		assertEquals(1, getContentResolver().delete(itemUri, null, null));
	
	}

	private ContentResolver getContentResolver() {
		return getContext().getContentResolver();
	}

}