package com.sunshine.support.sync;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.database.tables.APISyncStateTable;
import com.sunshine.metadata.database.tables.PackageTable;
import com.sunshine.metadata.database.tables.Table;

public class APISyncTask extends AsyncTask<String, String, Integer> {

	private Uri root_uri;
	private boolean isConnected;
	private Table syncTable;
	private HttpClient httpClient;
	private MetadataDBHandler dbHandler;
	private Context context;

	private static final String[] SYNCED_TABLES = { PackageTable.TABLE_NAME, };

	public APISyncTask(Context context, String ip) {
		this.context = context;
		this.root_uri = new Uri.Builder().scheme("http").authority(ip).build();

		httpClient = new DefaultHttpClient();
		isConnected = getConnectionStatus();

		dbHandler = new MetadataDBHandler(context);
		syncTable = dbHandler.getTableManager(APISyncStateTable.TABLE_NAME);
	}

	@Override
	protected Integer doInBackground(String... params) {
		while (isConnected) {
			for (String tableName: SYNCED_TABLES) {
				Table table = dbHandler.getTableManager(tableName);
				TableSynchronizer synchronizer = new TableSynchronizer(table, syncTable, httpClient, root_uri);
				if (!synchronizer.sync()) {
					isConnected = false;
					break;
				}
			}
 		}
		return null;
	}
	
	protected boolean getConnectionStatus() {
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork.isConnectedOrConnecting();
	}
}
