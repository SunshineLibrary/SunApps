package com.sunshine.support.sync;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.sunshine.metadata.database.DatabaseTestCase;
import com.sunshine.metadata.database.tables.APISyncStateTable;
import com.sunshine.metadata.database.tables.APISyncStateTable.APISyncState;
import com.sunshine.metadata.database.tables.PackageTable;
import com.sunshine.metadata.database.tables.Table;

public class TableSynchronizerTest extends DatabaseTestCase {

	private TableSynchronizer synchronizer;
	private Table syncStateTable;
	private Table table;
	private static Uri root_uri;

	static {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("127.0.0.1");
		root_uri = builder.build();
	}

	@Override
	public void setUp() {
		super.setUp();
		syncStateTable = dbHandler
				.getTableManager(APISyncStateTable.TABLE_NAME);
		table = dbHandler.getTableManager(PackageTable.TABLE_NAME);
		synchronizer = new TableSynchronizer(table, syncStateTable,
				new DefaultHttpClient(), root_uri);
	}

	@Override
	public void tearDown() {
		super.tearDown();
	}

	public void testGetLastUpdateTime() {
		assertEquals(0, synchronizer.getLastUpdateTime());
		ContentValues values = new ContentValues();
		values.put(APISyncState._LAST_UPDATE, 1234567);
		assertEquals(
				1,
				syncStateTable.update(null, values, APISyncState._TABLE_NAME
						+ "=?", new String[] { PackageTable.TABLE_NAME }));
		TableSynchronizer temp = new TableSynchronizer(table, syncStateTable,
				new DefaultHttpClient(), root_uri);
		assertEquals(1234567, temp.getLastUpdateTime());
	}

	public void testRequestURI() {
		String expected = "http://127.0.0.1/packages?timestamp=0";
		assertEquals(expected, synchronizer.getRequestURI());
	}

	public void testGetJSONArray() throws IOException, JSONException {
		String expected = "[{\"id\": 1, \"name\": \"package\", \"version\": 1}]";
		InputStream input = new ByteArrayInputStream(expected.getBytes());
		JSONArray arr = synchronizer.getJsonArrayFromInputStream(input);
		JSONObject row = arr.getJSONObject(0);
		assertEquals("1", row.getString("version"));
		assertEquals("package", row.get("name"));
		assertEquals(1, row.getInt("id"));
	}

	public void testProcessJSONArray() throws IOException, JSONException {
		String expected = "[{\"id\": 1, \"name\": \"package\", \"version\": 1, \"updated_at\": 1234567}]";
		InputStream input = new ByteArrayInputStream(expected.getBytes());
		JSONArray arr = synchronizer.getJsonArrayFromInputStream(input);
		assertTrue(synchronizer.processJsonArray(arr));
		
		//Test value inserted
		Cursor cursor = table.query(null, table.getColumns(), BaseColumns._ID
				+ "=?", new String[] { "1" }, null);
		assertTrue(cursor.moveToFirst());
		assertEquals(1, cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
		assertEquals("package", cursor.getString(cursor.getColumnIndex("name")));
		assertEquals("1", cursor.getString(cursor.getColumnIndex("version")));
	}
}
