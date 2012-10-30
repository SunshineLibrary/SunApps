package com.ssl.support.downloader.tasks;

import com.ssl.support.downloader.tasks.DownloadTask;
import com.ssl.support.utils.ListenableAsyncTask;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class AsyncDownloadTask extends ListenableAsyncTask{

    private DownloadTask mDownloadTask;

    public AsyncDownloadTask(DownloadTask downloadTask) {
        mDownloadTask = downloadTask;
    }

    @Override
    protected Integer doInBackground(Object... params) {
        mDownloadTask.run();
        return mDownloadTask.getResult();
    }
}
