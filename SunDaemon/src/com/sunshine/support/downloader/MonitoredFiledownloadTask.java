package com.sunshine.support.downloader;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.sunshine.metadata.provider.MetadataContract;

import static com.sunshine.metadata.provider.MetadataContract.Downloadable.STATUS;

public class MonitoredFileDownloadTask extends FileDownloadTask {

    private static final ContentValues NOT_DOWNLOADED = new ContentValues();
    private static final ContentValues DOWNLOADING = new ContentValues();
    private static final ContentValues DOWNLOADED = new ContentValues();

    private Uri updateUri;

    static {
        NOT_DOWNLOADED.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, STATUS.NOT_DOWNLOADED.ordinal());
        DOWNLOADING.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, STATUS.DOWNLOADING.ordinal());
        DOWNLOADED.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, STATUS.DOWNLOADED.ordinal());
    }

    public MonitoredFileDownloadTask(Context context, Uri remoteUri, Uri localUri, Uri updateUri) {
        super(context, remoteUri, localUri);
        this.updateUri = updateUri;
    }

    @Override
    protected void onPreExecute() {
        context.getContentResolver().update(updateUri, DOWNLOADING, null, null);
    }

    @Override
    protected void onPostExecute(Integer status) {
        if (status == SUCCESS) {
            context.getContentResolver().update(updateUri, DOWNLOADED, null, null);
        } else {
            context.getContentResolver().update(updateUri, NOT_DOWNLOADED, null, null);
        }
    }
}
