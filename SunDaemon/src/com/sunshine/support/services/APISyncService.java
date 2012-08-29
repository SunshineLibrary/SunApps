package com.sunshine.support.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.sunshine.support.sync.APISyncTask;

public class APISyncService extends Service {

	private boolean syncInProgress;
	private APISyncTask syncTask;
	private static final String IP = "ssl-mock.herokuapp.com";
	private static final long MIN_DELAY = 1200000;
	private ConnectivityManager cm;

    private static Runnable wakeUpRunner;
    private static Handler handler;

    @Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!syncInProgress) {
            Log.v(getClass().getName(), "Starting APISyncTask...");
			syncTask.execute();
			syncInProgress = true;
		}
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		syncInProgress = false;
		syncTask = new APISyncTask(this) {
			@Override
			protected void onPostExecute(Integer result) {
				if (result.intValue() == SUCCESS) {
                    Log.v(getClass().getName(), "API sync completed successfully.");
				} else {
                    Log.v(getClass().getName(), "API sync failed.");
                }
				stopSelf();
			}
		};
	}

	@Override
	public void onDestroy() {
		if (isConnected()) {
            long delay = (long) Math.floor(Math.random() * MIN_DELAY + MIN_DELAY);
            getHandler().postDelayed(getWakeUpRunner(), delay);
		}
	}

    private static Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    private Runnable getWakeUpRunner() {
        if (wakeUpRunner == null) {
            wakeUpRunner = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplication(), APISyncService.class);
                    getBaseContext().startService(intent);
                }
            };
        } else {
            getHandler().removeCallbacks(wakeUpRunner);
        }
        return wakeUpRunner;
    }

	public boolean isConnected() {
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            return cm.getActiveNetworkInfo().isConnected();
        } else {
            Log.v(getClass().getName(), "Device is not connected...");
            return false;
        }
	}
}