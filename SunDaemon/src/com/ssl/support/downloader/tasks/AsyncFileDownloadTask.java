package com.ssl.support.downloader.tasks;

import android.content.Context;
import android.net.Uri;
import com.ssl.support.utils.ListenableAsyncTask;

public class AsyncFileDownloadTask extends ListenableAsyncTask<Uri, Integer, Integer> {

    private FileDownloadTask fileDownloadTask;

    protected Context context;
    protected Uri remoteUri;
    protected Uri localUri;

    public AsyncFileDownloadTask(Context context, Uri remoteUri, Uri localUri) {
        this.context = context;
        this.remoteUri = remoteUri;
        this.localUri = localUri;
        fileDownloadTask = new FileDownloadTask(context, remoteUri, localUri);
        fileDownloadTask.setDownloadProgressListener(new FileDownloadTask.DownloadProgressListener() {
            @Override
            public void onProgressUpdate(int progress) {
                AsyncFileDownloadTask.this.onProgressUpdate(progress);
            }
        });
    }

    @Override
    protected Integer doInBackground(Uri... uris) {
        return fileDownloadTask.execute();
    }
}
