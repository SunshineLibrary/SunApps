package com.sunshine.support.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;

public class APISyncService extends Service {

	private boolean syncInProgress;
	private APISyncTask syncTask;
	private static final String IP = "ssl-mock.herokuapp.com";
	private static final long MIN_DELAY = 1200000;
	private ConnectivityManager cm;

	private static long lastSuccessfulSync;
	
	public static final String SERVICE_ACTION = "com.sunshine.support.action.sync";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!syncInProgress
				&& System.currentTimeMillis() > MIN_DELAY + lastSuccessfulSync) {
			syncTask.execute();
			syncInProgress = true;
		}
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		syncInProgress = false;
		syncTask = new APISyncTask(this, IP) {
			@Override
			protected void onPostExecute(Integer result) {
				if (result.intValue() == SYNC_SUCCESS) {
					lastSuccessfulSync = System.currentTimeMillis();
				}
				stopSelf();
			}
		};
	}

	@Override
	public void onDestroy() {
		if (isConnected()) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					Intent intent = new Intent(SERVICE_ACTION);
					getBaseContext().startService(intent);
				}
			}, (long) Math.floor(Math.random() * MIN_DELAY + MIN_DELAY));
		}
	}

	protected boolean isConnected() {
		return cm.getActiveNetworkInfo().isConnected();
	}
}
