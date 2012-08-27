package com.sunshine.support.sync;

import android.content.ContentValues;
import android.provider.BaseColumns;
import android.util.Log;
import com.sunshine.metadata.database.Table;
import com.sunshine.support.api.ApiClient;
import com.sunshine.support.data.models.ApiSyncState;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BasicTableSyncManager implements TableSyncManager {

	private static final int MAX_RETRY_COUNT = 3;
	private static final int BATCH_SIZE = 100;
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private Table table;
    private ApiSyncState syncState;

    public BasicTableSyncManager(Table table, ApiSyncState state) {
		this.table = table;
        this.syncState = state;
	}

	public boolean sync() {
		boolean isInSync = false;
		int retryCount = 0;
        HttpClient httpClient = ApiClient.newHttpClient();
		while (!isInSync && retryCount <= MAX_RETRY_COUNT) {
			String requestURI = getRequestURI();
            Log.i(this.getClassName(), requestURI);

            HttpGet request = new HttpGet(requestURI);
            InputStream result = null;
			try {
				result = httpClient.execute(request).getEntity().getContent();
				JSONArray jsonArr = getJsonArrayFromInputStream(result);
				isInSync = processJsonArray(jsonArr);
			} catch (IOException e) {
				Log.e(getClassName(), "Failed to get " + requestURI, e);
				retryCount++;
			} catch (JSONException e) {
				Log.e(getClassName(), "Failed to parse response", e);
				break;
			} catch (ParseException e) {
				Log.e(getClassName(), "Failed to parse update time", e);
				break;
			} finally {
                closeConnection(result);
            }
		}
		return retryCount <= MAX_RETRY_COUNT;
	}

    private void closeConnection(InputStream result) {
        if (result != null) {
            try {
                result.close();
            } catch (IOException e) {
                Log.e(getClassName(), "Error closing connection.", e);
            }
        }
    }

    protected String getRequestURI() {
		return ApiClient.getSyncRequestUrl(table.getTableName(), syncState.getLastUpdateTime());
	}

	protected JSONArray getJsonArrayFromInputStream(InputStream result)
			throws IOException, JSONException {
		StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(result);

		int bufferSize = 4096;
		int readCount, offset = 0;
		char[] buffer = new char[bufferSize];
		while ((readCount = reader.read(buffer, offset, bufferSize - offset)) > 0) {
            offset += readCount;
			if (offset >= bufferSize) {
				builder.append(buffer);
				offset = 0;
			}
		}
		if (offset > 0) {
			builder.append(buffer, 0, offset);
		}

		return new JSONArray(builder.toString());
	}

	protected boolean processJsonArray(JSONArray jsonArr) throws JSONException,
			ParseException {
        table.beginTransaction();
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject row = jsonArr.getJSONObject(i);
			insertOrUpdateRow(row);
            syncState.setLastUpdateTime(Math.max(syncState.getLastUpdateTime(), parseTime(row.getString("updated_at"))));
		}
        table.endTransaction();
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
				} else if (row.has(column)) {
					values.put(column, row.getString(column));
				} else {
                    Log.w(getClassName(), "Could not read column " + column + " for table " + table.getTableName());
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

	private String getClassName() {
		return this.getClass().getName();
	}
}
