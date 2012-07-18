package com.sunshine.support.sync;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.Uri;
import android.os.AsyncTask;

import com.sunshine.metadata.database.tables.Table;

public class APISyncTask extends AsyncTask<String, String, Integer> {

	private Uri root_uri;
	private boolean isConnected;
	private Table syncTable;
	private HttpClient httpClient;

	public APISyncTask(String ip, Table syncTable) {
		root_uri = new Uri.Builder().scheme("http").authority(ip).build();
		isConnected = true;
		httpClient = new DefaultHttpClient();
	}

	@Override
	protected Integer doInBackground(String... params) {
		while (isConnected) {
			break;
		}
		return null;
	}


}
