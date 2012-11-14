package com.ssl.support.api.record;

import android.content.Context;
import com.ssl.support.downloader.DownloadTaskParams;
import com.ssl.support.downloader.tasks.DownloadTask;
import com.ssl.support.utils.DatabaseQueue;
import com.ssl.support.utils.JSONSerializable;
import org.json.JSONObject;

public class UserRecordRequestQueue extends DatabaseQueue<UserRecordRequest> {

    public UserRecordRequestQueue(Context context, JSONSerializable.Factory<UserRecordRequest> installRecordFactory) {
        super(context, "user_record", installRecordFactory);
    }

    public void add(UserRecordRequest request) {
        super.add(request.toString());
    }
}