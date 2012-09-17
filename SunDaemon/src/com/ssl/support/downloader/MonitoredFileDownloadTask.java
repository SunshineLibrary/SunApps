package com.ssl.support.downloader;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.ssl.metadata.provider.MetadataContract;

public class MonitoredFileDownloadTask extends FileDownloadTask {

    private static final ContentValues NOT_DOWNLOADED = new ContentValues();
    private static final ContentValues DOWNLOADING = new ContentValues();
    private static final ContentValues DOWNLOADED = new ContentValues();

    private Uri updateUri;

    static {
        NOT_DOWNLOADED.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_NOT_DOWNLOADED);
        DOWNLOADING.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_DOWNLOADING);
        DOWNLOADED.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_DOWNLOADED);
    }

    public MonitoredFileDownloadTask(Context context, Uri remoteUri, Uri localUri, Uri updateUri) {
        super(context, remoteUri, localUri);
        this.updateUri = updateUri;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        context.getContentResolver().update(updateUri, DOWNLOADING, null, null);
        context.getContentResolver().notifyChange(updateUri, null);
    }

    @Override
    protected void onPostExecute(Integer status) {
        super.onPostExecute(status);
        if (status == SUCCESS) {
            context.getContentResolver().update(updateUri, DOWNLOADED, null, null);
        } else {
            context.getContentResolver().update(updateUri, NOT_DOWNLOADED, null, null);
        }
        context.getContentResolver().notifyChange(updateUri, null);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        context.getContentResolver().update(updateUri, downloadProgress(values[0]), null, null);
        context.getContentResolver().notifyChange(updateUri, null);
    }

    private ContentValues downloadProgress(int progress) {
        ContentValues values = new ContentValues();
        values.put(MetadataContract.Downloadable._DOWNLOAD_PROGRESS, progress);
        return values;
    }
}
