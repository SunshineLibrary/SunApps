package com.ssl.support.install.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class InstallService extends Service {

    public static final String ACTION_INSTALL = "com.ssl.support.action.install";
    public static final String ACTION_START_TIMER = "com.ssl.support.action.startTimer";
    public static final String ACTION_STOP_TIMER= "com.ssl.support.action.stopTimer";
    public static final String ACTION_SCHEDULE_INSTALL = "com.ssl.support.action.scheduleInstall";

    public static final String ACTION_PACKAGE_INSTALLED = "com.ssl.support.action.packageInstalled";
    public static final String ACTION_PACKAGE_INSTALL_FAILED = "com.ssl.support.action.packageInstallFailed";

    private static final String TAG = "Installer";
    private static final int INSTALL_DELAY = 10000;


    private InstallReceiver installReceiver;
    private InstallQueue installQueue;
    private boolean hasPendingInstall;
    private InstallTimer timer;
    private PowerManager.WakeLock wakeLock;

    private int numRunningTasks;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new InstallTimer(this, INSTALL_DELAY);
        installReceiver = new InstallReceiver();
        registerInstallReceivers();
        installQueue = new InstallQueue(getBaseContext(), new InstallRequest.Factory());
        hasPendingInstall = (installQueue.peek() != null);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
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
                    InstallTask installTask = new InstallTaskImpl(request.getApkPath());
                    numRunningTasks++;
                    installTask.execute();
                }
                releaseLock();
            } else {
                Log.w(getClass().getName(), "No pending install found. This is weird.");
            }
            hasPendingInstall = false;
        } else if(intent.getAction().equals(ACTION_START_TIMER)) {
            if (hasPendingInstall) {
                Log.v(getClass().getName(), "Found pending install. Starting timer...");
                timer.start();
                acquireLock();
            } else {
                Log.v(getClass().getName(), "No pending installs. Timer not started");
            }
        } else if(intent.getAction().equals(ACTION_STOP_TIMER)) {
            if (hasPendingInstall) {
                releaseLock();
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
        releaseLock();
        Log.i(getClass().getName(), "Stopping Installer Service...");
        unregisterReceiver(installReceiver);
        installQueue.release();
    }

    private void registerInstallReceivers() {
        Log.d(TAG, "Registering Intent Receivers");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        installReceiver = new InstallReceiver();
        registerReceiver(installReceiver, filter);
    }

    private void notifyInstall() {
        if (!((PowerManager) getSystemService(Context.POWER_SERVICE)).isScreenOn()) {
            Log.v(getClass().getName(), "Notifying receiver of new install request...");
            installReceiver.onReceive(this, new Intent(Intent.ACTION_SCREEN_OFF));
        }
    }

    private void acquireLock() {
        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }

    private void releaseLock() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    private void tryStopSelf() {
        if (!hasPendingInstall && numRunningTasks == 0) {
            stopSelf();
        }
    }

    private class InstallTaskImpl extends InstallTask {

        public InstallTaskImpl(Uri apkPath) {
            super(apkPath);
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            broadcastStatus(status);
            numRunningTasks--;
            tryStopSelf();
        }

        private void broadcastStatus(boolean status) {
            Intent intent;
            if (status) {
                intent = new Intent(ACTION_PACKAGE_INSTALLED, apkPath);
            } else {
                intent = new Intent(ACTION_PACKAGE_INSTALL_FAILED, apkPath);
            }
            Log.v(getClass().getName(), "Broadcasting install status: " + intent);
            sendBroadcast(intent);
        }
    }
}
