package com.sunshine.support.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.sunshine.metadata.database.tables.APISyncStateTable.APISyncState;
import com.sunshine.metadata.database.tables.Table;

public class TableSynchronizer {

	private static final int MAX_RETRY_COUNT = 3;
	private static final int BATCH_SIZE = 100;

	private Table syncStateTable;
	private Uri root_uri;
	private Table table;
	private HttpClient httpClient;
	private long lastUpdateTime;

	public TableSynchronizer(Table table, Table syncStatesTable,
			HttpClient httpClient, Uri root_uri) {
		this.root_uri = root_uri;
		this.table = table;
		this.syncStateTable = syncStatesTable;
		this.httpClient = httpClient;
		lastUpdateTime = getLastUpdateTimeFromDB();
	}

	private String getClassName() {
		return this.getClass().getName();
	}

	protected ContentValues getContentValuesFromRow(JSONObject row) {
		ContentValues values = new ContentValues();
		for (String column : table.getColumns()) {
			try {
				if (column.equals(BaseColumns._ID)) {
					values.put(column, row.getInt("id"));
				} else {
					values.put(column, row.getString(column));
				}
			} catch (JSONException e) {
				Log.e(getClassName(), "Failed to read column " + column, e);
			}
		}
		return values;
	}

	private JSONArray getJsonArrayFromInputStream(InputStream result)
			throws IOException, JSONException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(result));

		int bufferSize = 1024;
		int readCount, totalReadCount = 0;
		char[] buffer = new char[bufferSize];
		while ((readCount = reader.read(buffer, totalReadCount, bufferSize
				- totalReadCount)) > 0) {
			totalReadCount += readCount;
			if (totalReadCount >= bufferSize) {
				builder.append(buffer);
				totalReadCount = 0;
			}
		}
		if (totalReadCount > 0) {
			builder.append(buffer, 0, totalReadCount);
		}

		return new JSONArray(builder.toString());
	}

	private long getLastUpdateTimeFromDB() {
		Cursor cursor = syncStateTable.query(null, syncStateTable.getColumns(),
				APISyncState._TABLE_NAME + "=?", new String[]{syncStateTable.getTableName()},
				null);
		if (cursor.moveToFirst()) {
			return cursor.getLong(cursor
					.getColumnIndex(APISyncState._LAST_UPDATE));
		} else {
			updateLastUpdateTime(0);
			return 0;
		}
	}
	
	private void updateLastUpdateTime(long time) {
		ContentValues values = new ContentValues();
		values.put(APISyncState._TABLE_NAME, table.getTableName());
		values.put(APISyncState._LAST_UPDATE, time);
		syncStateTable.insert(null, values);
	}

	private String getRequestURI() {
		return root_uri.buildUpon().appendPath(table.getTableName())
				.appendQueryParameter("timestamp", "" + lastUpdateTime).build()
				.toString();
	}

	private void insertOrUpdateRow(JSONObject row) {
		ContentValues values = getContentValuesFromRow(row);
		if (table.update(null, values,
				BaseColumns._ID + "=" + values.getAsString(BaseColumns._ID),
				null) == 0) {
			table.insert(null, values);
		}
	}

	private boolean processJsonArray(JSONArray jsonArr) throws JSONException {
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject row = jsonArr.getJSONObject(i);
			insertOrUpdateRow(row);
			lastUpdateTime = Math.max(lastUpdateTime, row.getLong("updated_at"));
		}
		return jsonArr.length() < BATCH_SIZE;
	}

	public void sync() {
		boolean isInSync = false;
		int retryCount = 0;
		while (!isInSync && retryCount <= MAX_RETRY_COUNT) {
			String requestURI = getRequestURI();
			try {
				HttpUriRequest request = new HttpGet(requestURI);
				InputStream result = httpClient.execute(request).getEntity()
						.getContent();

				JSONArray jsonArr = getJsonArrayFromInputStream(result);

				isInSync = processJsonArray(jsonArr);
				
				updateLastUpdateTime(lastUpdateTime);
			} catch (IOException e) {
				Log.e(this.getClass().getName(), "Failed to get " + requestURI,
						e);
				retryCount++;
			} catch (JSONException e) {
				Log.e(this.getClass().getName(), "Failed to parse response", e);
				break;
			}
		}
	}
	
	protected long getLastUpdateTime() {
		return lastUpdateTime;
	}
}
