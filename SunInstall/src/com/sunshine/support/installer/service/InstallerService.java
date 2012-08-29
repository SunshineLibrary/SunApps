package com.sunshine.support.installer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class InstallerService extends Service {

    public static final String ACTION_INSTALL = "com.sunshine.support.action.install";
    public static final String ACTION_START_TIMER = "com.sunshine.support.action.startTimer";
    public static final String ACTION_STOP_TIMER= "com.sunshine.support.action.stopTimer";
    public static final String ACTION_SCHEDULE_INSTALL = "com.sunshine.support.action.scheduleInstall";


	private static final String TAG = "Installer";
    private static final int INSTALL_DELAY = 10000;


    private InstallerReceiver installReceiver;
    private InstallQueue installQueue;
    private boolean hasPendingInstall;
    private InstallTimer timer;


    @Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
        timer = new InstallTimer(this, INSTALL_DELAY);
        installReceiver = new InstallerReceiver();
        registerInstallReceivers();
        installQueue = new InstallQueue(getBaseContext(), new InstallRequest.Factory());
        hasPendingInstall = (installQueue.peek() != null);
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction().equals(ACTION_SCHEDULE_INSTALL)){
            Log.i(TAG, "Scheduling Install: "+intent);
            installQueue.add(new InstallRequest(intent.getData().toString()));
            hasPendingInstall = true;
            notifyInstall();
        } else if(intent.getAction().equals(ACTION_INSTALL)) {
            if (hasPendingInstall) {
                InstallRequest request;
                while ((request = installQueue.pop()) != null) {
                    Log.i(TAG, "Starting install: "+intent);
                    InstallTask installTask = new InstallTask();
                    installTask.execute(request.getApkPath());
                }
            } else {
                Log.w(getClass().getName(), "No pending install found. This is weird.");
            }
            hasPendingInstall = false;
        } else if(intent.getAction().equals(ACTION_START_TIMER)) {
            if (hasPendingInstall) {
                Log.v(getClass().getName(), "Found pending install. Starting timer...");
                timer.start();
            } else {
                Log.v(getClass().getName(), "No pending installs. Timer not started");
            }
        } else if(intent.getAction().equals(ACTION_STOP_TIMER)) {
            if (hasPendingInstall) {
                Log.v(getClass().getName(), "Found active timer. stopping...");
                timer.reset();
            } else {
                Log.v(getClass().getName(), "No active timer.");
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(getClass().getName(), "Stopping Installer Service...");
        unregisterReceiver(installReceiver);
        installQueue.release();
    }

    private void registerInstallReceivers() {
        Log.d(TAG, "Registering Intent Receivers");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        installReceiver = new InstallerReceiver();
        registerReceiver(installReceiver, filter);
    }

    private void notifyInstall() {
        if (!((PowerManager) getSystemService(Context.POWER_SERVICE)).isScreenOn()) {
            Log.v(getClass().getName(), "Notifying receiver of new install request...");
            installReceiver.onReceive(this, new Intent(Intent.ACTION_SCREEN_OFF));
        }
    }
}