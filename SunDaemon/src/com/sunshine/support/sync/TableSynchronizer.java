package com.sunshine.support.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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

	protected long getLastUpdateTime() {
		return lastUpdateTime;
	}

	private long getLastUpdateTimeFromDB() {
		long lastUpdate = 0;
		Cursor cursor = syncStateTable.query(null, syncStateTable.getColumns(),
				APISyncState._TABLE_NAME + "=?",
				new String[] { table.getTableName() }, null);
		if (cursor.moveToFirst()) {
			lastUpdate = cursor.getLong(cursor
					.getColumnIndex(APISyncState._LAST_UPDATE));
		}
		cursor.close();
		return lastUpdate;
	}

	public boolean sync() {
		boolean isInSync = false;
		int retryCount = 0;
		while (!isInSync && retryCount <= MAX_RETRY_COUNT) {
			String requestURI = getRequestURI();
			try {
				HttpGet request = new HttpGet(requestURI);
				Log.i(this.getClassName(), requestURI);
				HttpResponse response = httpClient.execute(request);
				InputStream result = response.getEntity().getContent();

				JSONArray jsonArr = getJsonArrayFromInputStream(result);
				isInSync = processJsonArray(jsonArr);
				updateLastUpdateTime(lastUpdateTime);

			} catch (IOException e) {
				Log.e(getClassName(), "Failed to get " + requestURI, e);
				retryCount++;
			} catch (JSONException e) {
				Log.e(getClassName(), "Failed to parse response", e);
				break;
			} catch (ParseException e) {
				Log.e(getClassName(), "Failed to parse update time", e);
				break;
			}
		}
		return retryCount <= MAX_RETRY_COUNT;
	}

	protected String getRequestURI() {
		return root_uri.buildUpon().appendPath(table.getTableName() + ".json")
				.appendQueryParameter("timestamp", "" + lastUpdateTime).build()
				.toString();
	}

	protected JSONArray getJsonArrayFromInputStream(InputStream result)
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

	protected boolean processJsonArray(JSONArray jsonArr) throws JSONException,
			ParseException {
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject row = jsonArr.getJSONObject(i);
			insertOrUpdateRow(row);
            lastUpdateTime = Math.max(lastUpdateTime, parseTime(row.getString("updated_at")));
		}
		return jsonArr.length() < BATCH_SIZE;
	}

    private long parseTime(String updated_at) throws ParseException, JSONException {
        return TIME_FORMAT.parse(updated_at).getTime();
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

	private void insertOrUpdateRow(JSONObject row) {
		ContentValues values = getContentValuesFromRow(row);
		if (table.update(null, values,
				BaseColumns._ID + "=" + values.getAsString(BaseColumns._ID),
				null) == 0) {
			table.insert(null, values);
		}
	}

	private void updateLastUpdateTime(long time) {
		ContentValues values = new ContentValues();
		values.put(APISyncState._TABLE_NAME, table.getTableName());
		values.put(APISyncState._LAST_UPDATE, time);
		syncStateTable.insert(null, values);
	}

	private String getClassName() {
		return this.getClass().getName();
	}
}
