package com.ssl.support.services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.ssl.support.downloader.DownloadQueue;
import com.ssl.support.downloader.DownloadTaskParams;
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
    private boolean downloading;
    public static final String PARAMS_KEY = "task_params";

    @Override
    public void onCreate() {
        super.onCreate();
        downloadQueue = new DownloadQueue(getBaseContext(), new MonitoredFileDownloadTask.Factory(getBaseContext()));
        downloading = false;
        startNextListener = new Listener<Integer>() {
            @Override
            public void onResult(Integer integer) {
                MonitoredFileDownloadTask task = downloadQueue.pop();
                Log.d("DownloadService", "Completed Task: " + task.toJSON());
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
        if (intent.hasExtra(PARAMS_KEY)) {
            DownloadTaskParams params = (DownloadTaskParams) intent.getParcelableExtra(PARAMS_KEY);
            downloadQueue.add(params.remoteUri, params.localUri, params.updateUri, params.notifyUri);
        }

        startNextTask();
        return START_STICKY;
    }

    private void startNextTask() {
        if (!downloading) {
            MonitoredFileDownloadTask task = downloadQueue.peek();
            if (task != null) {
                Log.d("DownloadService", "Starting Task: " + task.toJSON());
                task.addListener(startNextListener);
                downloading = true;
                task.execute();
            } else {
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DownloadService", "Stopping");
        downloadQueue.release();
    }
}
