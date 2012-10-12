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

    public static final String ACTION_SCHEDULE_UNINSTALL = "com.ssl.support.action.scheduleUninstall";

    public static final String ACTION_PACKAGE_INSTALLED = "com.ssl.support.action.packageInstalled";
    public static final String ACTION_PACKAGE_INSTALL_FAILED = "com.ssl.support.action.packageInstallFailed";
    public static final String ACTION_INSTALL_STOPPED = "com.ssl.support.action.installStopped";

    public static final String ACTION_PACKAGE_UNINSTALLED = "com.ssl.support.action.packageUninstalled";
    public static final String ACTION_PACKAGE_UNINSTALL_FAILED = "com.ssl.support.action.packageUninstallFailed";
    public static final String ACTION_UNINSTALL_STOPPED = "com.ssl.support.action.uninstallStopped";

  
    private static final String TAG = "Installer";
    private static final int INSTALL_DELAY = 10000;


    private InstallReceiver installReceiver;
    private InstallQueue installQueue;
    private UninstallQueue uninstallQueue;
    private boolean hasActiveTimer;
    private boolean installInProgress;
    private InstallTimer timer;
    private PowerManager.WakeLock wakeLock;


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
        uninstallQueue = new UninstallQueue(getBaseContext(), new UninstallRequest.Factory());
        hasActiveTimer = (installQueue.peek() != null) || (uninstallQueue.peek() != null);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction().equals(ACTION_SCHEDULE_INSTALL)){
            Log.i(TAG, "Scheduling Install: "+intent);
            installQueue.add(new InstallRequest(intent.getData().toString()));
            if (!installInProgress) {
                hasActiveTimer = true;
                notifyInstall();
            }
        } else if(intent.getAction().equals(ACTION_SCHEDULE_UNINSTALL) && intent.hasExtra("pkg")){
            Log.i(TAG, "Scheduling Uninstall: "+intent);
            uninstallQueue.add(new UninstallRequest(intent.getStringExtra("pkg")));
            if (!installInProgress) {
                hasActiveTimer = true;
                notifyInstall();
            }
        } else if(intent.getAction().equals(ACTION_INSTALL)) {
            Log.i(TAG, "Starting install: "+intent);
            if (hasActiveTimer) {
                hasActiveTimer = false;
                startNextInstall();
                installInProgress = true;
            } else {
                Log.w(getClass().getName(), "No pending install found. This is weird.");
                releaseLock();
                stopSelf();
            }
        } else if(intent.getAction().equals(ACTION_START_TIMER)) {
            if (hasActiveTimer) {
                Log.v(getClass().getName(), "Found pending install. Starting timer...");
                timer.start();
                acquireLock();
            } else {
                Log.v(getClass().getName(), "No pending installs. Timer not started");
            }
        } else if(intent.getAction().equals(ACTION_STOP_TIMER)) {
            if (hasActiveTimer) {
                releaseLock();
                Log.v(getClass().getName(), "Found active timer. stopping...");
                timer.reset();
            } else {
                Log.v(getClass().getName(), "No active timer.");
            }
        }
        return START_STICKY;
    }

    private void startNextInstall() {
        InstallRequest request = installQueue.peek();
        UninstallRequest request2 = uninstallQueue.peek();
        if (request2 != null) {
            UninstallTask uninstallTask = new UninstallTaskImpl(request2.getApkPkg());
            uninstallTask.execute();
        }else if (request != null){
            InstallTask installTask = new InstallTaskImpl(request.getApkPath());
            installTask.execute();
        } else {
            installInProgress = false;
            releaseLock();
            sendBroadcast(new Intent(ACTION_INSTALL_STOPPED));
            sendBroadcast(new Intent(ACTION_UNINSTALL_STOPPED));
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseLock();
        Log.i(getClass().getName(), "Stopping Installer Service...");
        unregisterReceiver(installReceiver);
        installQueue.release();
        uninstallQueue.release();
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

    private class InstallTaskImpl extends InstallTask {

        public InstallTaskImpl(Uri apkPath) {
            super(apkPath);
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            broadcastStatus(status);
            installQueue.pop();
            startNextInstall();
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
    
    private class UninstallTaskImpl extends UninstallTask {

        public UninstallTaskImpl(String apkPkg) {
            super(apkPkg);
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            broadcastStatus(status);
            uninstallQueue.pop();
            startNextInstall();
        }

        private void broadcastStatus(boolean status) {
            Intent intent;
            if (status) {
                intent = new Intent(ACTION_PACKAGE_UNINSTALLED).putExtra("pkg", apkPkg);
            } else {
                intent = new Intent(ACTION_PACKAGE_UNINSTALL_FAILED).putExtra("pkg", apkPkg);
            }
            Log.v(getClass().getName(), "Broadcasting uninstall status: " + intent);
            sendBroadcast(intent);
        }
    }

}
