package com.ssl.support.install.service;

import android.content.Context;
import com.ssl.support.install.utils.JSONSerializable;
import com.ssl.support.install.utils.DatabaseQueue;

public class InstallQueue extends DatabaseQueue<InstallRequest> {

    public InstallQueue(Context context, JSONSerializable.Factory<InstallRequest> installRecordFactory) {
        super(context, "install_queue", installRecordFactory);
    }
}