package com.ssl.support.downloader;

import android.content.Context;
import android.net.Uri;
import com.ssl.support.downloader.tasks.DownloadTask;
import com.ssl.support.utils.DatabaseQueue;
import com.ssl.support.utils.JSONSerializable;
import org.json.JSONObject;

public class DownloadQueue extends DatabaseQueue<DownloadTask> {

    public DownloadQueue(Context context, JSONSerializable.Factory<DownloadTask> installRecordFactory) {
        super(context, "download_queue", installRecordFactory);
    }

    public void add(DownloadTaskParams params) {
        JSONObject object = DownloadTask.toJSON(params.type, params.id);
        super.add(object.toString());
    }
}