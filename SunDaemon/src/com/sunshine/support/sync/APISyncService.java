package com.sunshine.support.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.sunshine.metadata.database.MetadataDBHandler;

public class APISyncService extends Service {
	
	private MetadataDBHandler dbHandler;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
	}
	
	@Override
	public void onCreate() {
		dbHandler = new MetadataDBHandler(getBaseContext());
	}
}
