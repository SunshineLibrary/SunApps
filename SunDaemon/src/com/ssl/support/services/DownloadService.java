package com.ssl.support.services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import com.ssl.support.downloader.DownloadQueue;
import com.ssl.support.downloader.MonitoredFileDownloadTask;
import com.ssl.support.utils.Listener;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Bowen Sun
 * @version 1.0
 */

/*
 * Serialize Download Requests
 */
public class DownloadService extends Service {

    private DownloadQueue downloadQueue;
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
        downloadQueue = new DownloadQueue(getBaseContext(), new MonitoredFileDownloadTask.Factory(getBaseContext()));
        downloading = false;
        startNextListener = new Listener<Integer>() {
            @Override
            public void onResult(Integer integer) {
                downloading = false;
                downloadQueue.pop();
                startNextTask();
            }
        };
        mBinder = new DownloadBinder();
        startNextTask();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void addDownloadTask(Uri remoteUri, Uri localUri, Uri updateUri, Uri notifyUri) {
        downloadQueue.add(remoteUri, localUri, updateUri, notifyUri);
        startNextTask();
    }

    private synchronized void startNextTask() {
        if (!downloading) {
            MonitoredFileDownloadTask task = downloadQueue.peek();
            if (task != null) {
                task.addListener(startNextListener);
                downloading = true;
                task.execute();
            }
        }
    }
}
