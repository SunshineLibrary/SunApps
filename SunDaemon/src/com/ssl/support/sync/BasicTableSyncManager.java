package com.ssl.support.sync;

import android.content.ContentValues;
import android.provider.BaseColumns;
import android.util.Log;
import com.ssl.metadata.database.Table;
import com.ssl.support.api.ApiClient;
import com.ssl.support.data.models.ApiSyncState;
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
    private static final long TIME_ZERO = parseTime("1970-01-01T08:00:00+08:00");

    private Table table;
    private ApiSyncState syncState;
    private ApiClient apiClient;

    public BasicTableSyncManager(Table table, ApiSyncState state, ApiClient apiClient) {
		this.table = table;
        this.syncState = state;
        this.apiClient = apiClient;
	}

	public boolean sync() {
		boolean isInSync = false;
		int retryCount = 0;
        HttpClient httpClient = apiClient.newHttpClient();
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
		return isInSync;
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
		return apiClient.getSyncRequestUrl(table.getTableName(), syncState.lastUpdateTime);
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
        try {
            table.getDatabase().beginTransaction();
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject row = jsonArr.getJSONObject(i);
                insertOrUpdateRow(row);
                syncState.lastUpdateTime = Math.max(syncState.lastUpdateTime, parseTime(row.getString("updated_at")));
            }
            table.getDatabase().setTransactionSuccessful();
        } finally {
            table.getDatabase().endTransaction();
        }
		return jsonArr.length() < BATCH_SIZE;
	}

    private static long parseTime(String time) {
        try {
            return TIME_FORMAT.parse(time).getTime();
        } catch (ParseException e) {
            Log.e(BasicTableSyncManager.class.getName(), "Failed to parse time " + time, e);
        }
        return 0;
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

	private void insertOrUpdateRow(JSONObject row) throws JSONException {
        if (deleted(row)) {
            table.delete(null, BaseColumns._ID + "=?", new String[]{row.getString("id")});
        } else {
            ContentValues values = getContentValuesFromRow(row);
            if (table.update(null, values,
                    BaseColumns._ID + "=" + values.getAsString(BaseColumns._ID), null) == 0) {
                table.insert(null, values);
            }
        }
	}

	private String getClassName() {
		return this.getClass().getName();
	}

    private boolean deleted(JSONObject row) {
        try {
            long time = parseTime(row.getString("created_at"));
            return  time == 0 || time == TIME_ZERO;
        } catch (JSONException e) {
            Log.e(getClassName(), "Failed to get created_at " + row.toString(), e);
        }
        return false;
    }
}
