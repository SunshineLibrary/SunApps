package com.sunshine.support.installer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import org.apache.http.entity.InputStreamEntity;

public class InstallerService extends Service {

    public static final String ACTION_INSTALL = "com.sunshine.support.action.install";
    public static final String ACTION_START_TIMER = "com.sunshine.support.action.startTimer";
    public static final String ACTION_STOP_TIMER= "com.sunshine.support.action.stopTimer";
    public static final String ACTION_SCHEDULE_INSTALL = "com.sunshine.support.action.scheduleInstall";


	private static final String TAG = "Installer";
    private InstallQueue installQueue;
    private BroadcastReceiver installReceiver;
    private Handler handler;

    private static final int INSTALL_DELAY = 10000;
    private InstallTimer timer;


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
        installQueue = new InstallQueue(getBaseContext(), new InstallRequest.Factory());
        handler = new Handler();
        registerIntentReceivers();
        if (installQueue.peek() != null) {
            notifyInstall();
        }
	}
	
	@Override  
    public void onStart(Intent intent, int startId) {
		if(intent.getAction().equals(ACTION_SCHEDULE_INSTALL)){
			Log.i(TAG, "Scheduling Install: "+intent);
            installQueue.add(new InstallRequest(intent.getData().toString()));
            notifyInstall();
		} else if(intent.getAction().equals(ACTION_INSTALL)) {
            InstallRequest request;
            while ((request = installQueue.pop()) != null) {
                Log.i(TAG, "Starting install: "+intent);
                InstallTask installTask = new InstallTask();
                installTask.execute(request.getApkPath());
            }
            stopSelf();
		} else if(intent.getAction().equals(ACTION_START_TIMER)) {
            if (timer == null) {
                Log.v(getClass().getName(), "Receiver detected install request, starting timer");
                timer = new InstallTimer(this);
                new Handler().postDelayed(timer, INSTALL_DELAY);
            }
        } else if(intent.getAction().equals(ACTION_STOP_TIMER)) {
            if (timer != null) {
                Log.v(getClass().getName(), "Receiver detected screen on, revoking timer");
                handler.removeCallbacks(timer);
                timer = null;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(installReceiver);
        installQueue.release();
        Log.i(getClass().getName(), "Stopping Installer Service...");
    }

    private void registerIntentReceivers()
	{
        Log.d(TAG, "Registering Intent Receivers");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        installReceiver = new InstallerReceiver();
        registerReceiver(installReceiver, filter);
	}

    private void notifyInstall() {
        if (!((PowerManager) getSystemService(Context.POWER_SERVICE)).isScreenOn()) {
            installReceiver.onReceive(getBaseContext(), new Intent(Intent.ACTION_SCREEN_OFF));
        }
    }
}
