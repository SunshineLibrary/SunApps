package com.sunshine.support.installer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class InstallerService extends Service {

    public final String ACTION_INSTALL = "com.sunshine.support.action.install";
    public final String ACTION_SCHEDULE_INSTALL = "com.sunshine.support.action.scheduleInstall";

	private static final String TAG = "Installer";
    private InstallQueue installQueue;
    private BroadcastReceiver installReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
        installQueue = new InstallQueue(getBaseContext(), new InstallRequest.Factory());
        if (installQueue.peek() != null) {
            registerIntentReceivers();
        }
	}
	
	@Override  
    public void onStart(Intent intent, int startId) {
		if(intent.getAction().equals("com.sunshine.support.action.scheduleInstall")){
			Log.i(TAG, "Scheduling Install: "+intent);
            installQueue.add(new InstallRequest(intent.getData().toString()));
            registerIntentReceivers();
		}else if(intent.getAction().equals("com.sunshine.support.action.install")){
            InstallRequest request;
            while ((request = installQueue.pop()) != null) {
                Log.i(TAG, "Starting install: "+intent);
                InstallTask installTask = new InstallTask();
                installTask.execute(request.getApkPath());
            }
            stopSelf();
		}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(installReceiver);
        Log.i(getClass().getName(), "Stopping Installer Service...");
    }

    private void registerIntentReceivers()
	{
        if (installReceiver == null) {
            Log.d(TAG, "Registering Intent Receivers");
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            installReceiver = new InstallerReceiver();
            registerReceiver(installReceiver, filter);
        }
	} 
}
