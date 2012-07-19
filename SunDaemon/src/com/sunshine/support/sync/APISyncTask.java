package com.sunshine.support.sync;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.Uri;
import android.os.AsyncTask;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.database.tables.APISyncStateTable;
import com.sunshine.metadata.database.tables.PackageTable;
import com.sunshine.metadata.database.tables.Table;

public class APISyncTask extends AsyncTask<String, String, Integer> {

	private Uri root_uri;
	private Table syncTable;
	private HttpClient httpClient;
	private MetadataDBHandler dbHandler;
	private APISyncService context;

	private static final String[] SYNCED_TABLES = { PackageTable.TABLE_NAME, };

	public static final int SYNC_SUCCESS = 0;
	public static final int SYNC_FAILURE = -1;
	
	public APISyncTask(APISyncService context, String ip) {
		this.context = context;
		this.root_uri = new Uri.Builder().scheme("http").authority(ip).build();

		httpClient = new DefaultHttpClient();

		dbHandler = new MetadataDBHandler(context);
		syncTable = dbHandler.getTableManager(APISyncStateTable.TABLE_NAME);
	}

	@Override
	protected Integer doInBackground(String... params) {
		int status = SYNC_SUCCESS;
		if (isConnected()) {
			//TODO: should check for changed tables before sync.
			for (String tableName: SYNCED_TABLES) {
				Table table = dbHandler.getTableManager(tableName);
				TableSynchronizer synchronizer = new TableSynchronizer(table, syncTable, httpClient, root_uri);
				if (!synchronizer.sync() ) {
					status = SYNC_FAILURE;
					break;
				}
			}
 		}
		return status;
	}
	
	protected boolean isConnected() {
		return context.isConnected();
	}
}
