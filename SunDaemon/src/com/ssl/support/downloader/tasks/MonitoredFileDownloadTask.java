package com.ssl.support.downloader.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.downloader.tasks.AsyncFileDownloadTask;
import com.ssl.support.downloader.tasks.FileDownloadTask;

public class MonitoredFileDownloadTask extends AsyncFileDownloadTask {

    private static final ContentValues NOT_DOWNLOADED = new ContentValues();
    private static final ContentValues DOWNLOADING = new ContentValues();
    private static final ContentValues DOWNLOADED = new ContentValues();

    private Uri updateUri;
    private Uri notifyUri;

    static {
        NOT_DOWNLOADED.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_NOT_DOWNLOADED);
        DOWNLOADING.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_DOWNLOADING);
        DOWNLOADED.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_DOWNLOADED);
    }

    public MonitoredFileDownloadTask(Context context, Uri remoteUri, Uri localUri, Uri updateUri) {
        super(context, remoteUri, localUri);
        this.updateUri = updateUri;
    }

    public MonitoredFileDownloadTask(Context context, Uri remoteUri, Uri localUri, Uri updateUri, Uri notifyUri) {
        super(context, remoteUri, localUri);
        this.updateUri = updateUri;
        this.notifyUri = notifyUri;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (remoteUri == null || localUri == null) {
            onPostExecute(FileDownloadTask.SUCCESS);
            cancel(true);
        } else {
            context.getContentResolver().update(updateUri, DOWNLOADING, null, null);
            context.getContentResolver().notifyChange(updateUri, null);
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        super.onPostExecute(status);
        if (status == FileDownloadTask.SUCCESS) {
            context.getContentResolver().update(updateUri, DOWNLOADED, null, null);
        } else {
            context.getContentResolver().update(updateUri, NOT_DOWNLOADED, null, null);
        }
        if (notifyUri != null) {
            context.getContentResolver().notifyChange(notifyUri, null);
        } else {
            context.getContentResolver().notifyChange(updateUri, null);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        context.getContentResolver().update(updateUri, downloadProgress(values[0]), null, null);
        if (notifyUri != null) {
            context.getContentResolver().notifyChange(notifyUri, null);
        } else {
            context.getContentResolver().notifyChange(updateUri, null);
        }
    }

    private ContentValues downloadProgress(int progress) {
        ContentValues values = new ContentValues();
        values.put(MetadataContract.Downloadable._DOWNLOAD_PROGRESS, progress);
        return values;
    }
}
