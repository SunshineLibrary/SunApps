package com.ssl.support.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import com.ssl.support.downloader.DownloadQueue;
import com.ssl.support.downloader.DownloadTaskFactory;
import com.ssl.support.downloader.DownloadTaskParams;
import com.ssl.support.downloader.tasks.AsyncDownloadTask;
import com.ssl.support.downloader.tasks.DownloadTask;
import com.ssl.support.utils.Listener;

/**
 * @author Bowen Sun
 * @version 1.0
 */

/*
 * Serialize Download Requests
 */
public class DownloadService extends Service {

    private PowerManager.WakeLock wakeLock;

    private DownloadQueue downloadQueue;
    private Listener<Integer> startNextListener;
    private boolean downloading;
    public static final String PARAMS_KEY = "task_params";

    @Override
    public void onCreate() {
        super.onCreate();
        downloadQueue = new DownloadQueue(getBaseContext(), new DownloadTaskFactory(getBaseContext()));
        downloading = false;

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());

        startNextListener = new Listener<Integer>() {
            @Override
            public void onResult(Integer integer) {
                AsyncDownloadTask task = new AsyncDownloadTask(downloadQueue.pop());
                downloading = false;
                startNextTask();
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("DownloadService", intent.toString());
        if (intent.hasExtra(PARAMS_KEY)) {
            DownloadTaskParams params = (DownloadTaskParams) intent.getParcelableExtra(PARAMS_KEY);
            downloadQueue.add(params);
            acquireLock();
        }
        startNextTask();
        return START_STICKY;
    }

    private void startNextTask() {
        if (!downloading) {
            DownloadTask task = downloadQueue.peek();
            if (task != null) {
                AsyncDownloadTask asyncTask = new AsyncDownloadTask(task);
                Log.d("DownloadService", "Starting Task: " + task.toJSON());
                asyncTask.addListener(startNextListener);
                downloading = true;
                asyncTask.execute();
            } else {
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseLock();
        Log.d("DownloadService", "Stopping");
        downloadQueue.release();
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
}
