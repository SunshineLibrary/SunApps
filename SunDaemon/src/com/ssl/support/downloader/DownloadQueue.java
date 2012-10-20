package com.ssl.support.downloader;

import android.content.Context;
import android.net.Uri;
import com.ssl.support.utils.DatabaseQueue;
import com.ssl.support.utils.JSONSerializable;
import org.json.JSONObject;

public class DownloadQueue extends DatabaseQueue<MonitoredFileDownloadTask> {

    public DownloadQueue(Context context, JSONSerializable.Factory<MonitoredFileDownloadTask> installRecordFactory) {
        super(context, "download_queue", installRecordFactory);
    }

    public void add(Uri remoteUri, Uri localUri, Uri updateUri, Uri notifyUri) {
        JSONObject object = MonitoredFileDownloadTask.toJSON(remoteUri, localUri, updateUri, notifyUri);
        super.add(object.toString());
    }
}