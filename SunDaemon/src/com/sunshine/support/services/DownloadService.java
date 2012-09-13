package com.sunshine.support.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.sunshine.support.downloader.MonitoredFileDownloadTask;
import com.sunshine.support.utils.Listener;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Bowen Sun
 * @version 1.0
 */

/*
 * Serialize Download Requests
 */
public class DownloadService extends Service {

    private LinkedBlockingQueue<MonitoredFileDownloadTask> downloadTasks;
    private Listener<Integer> startNextListener;
    private DownloadBinder mBinder;
    private boolean downloading;


    public class DownloadBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadTasks = new LinkedBlockingQueue<MonitoredFileDownloadTask>();
        downloading = false;
        startNextListener = new Listener<Integer>() {
            @Override
            public void onResult(Integer integer) {
                startNextTask();
                downloading = false;
            }
        };
        mBinder = new DownloadBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public synchronized void addDownloadTask(MonitoredFileDownloadTask downloadTask) {
        downloadTasks.add(downloadTask);
        if (!downloading) {
            startNextTask();
        }
    }

    private void startNextTask() {
        MonitoredFileDownloadTask task = downloadTasks.poll();
        if (task != null) {
            task.addListener(startNextListener);
            downloading = true;
            task.execute();
        }
    }
}
