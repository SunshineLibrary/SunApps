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
import com.ssl.support.utils.LockManager;

/**
 * @author Bowen Sun
 * @version 1.0
 */

/*
 * Serialize Download Requests
 */
public class DownloadService extends Service {

    private LockManager lockManager;
    private LockManager.Token lockToken;

    private DownloadQueue downloadQueue;
    private Listener<Integer> startNextListener;
    private boolean downloading;
    public static final String PARAMS_KEY = "task_params";

    @Override
    public void onCreate() {
        super.onCreate();
        downloadQueue = new DownloadQueue(getBaseContext(), new DownloadTaskFactory(getBaseContext()));
        downloading = false;

        lockManager = LockManager.getInstance(this);

        startNextListener = new Listener<Integer>() {
            @Override
            public void onResult(Integer integer) {
                downloadQueue.pop();
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
        lockToken = lockManager.acquireWifiLock(lockToken);
    }

    private void releaseLock() {
        lockManager.releaseLock(lockToken);
    }
}
