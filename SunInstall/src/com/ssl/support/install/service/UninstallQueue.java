package com.ssl.support.install.service;

import android.content.Context;

import com.ssl.support.install.utils.DatabaseQueue;
import com.ssl.support.install.utils.JSONSerializable;

public class UninstallQueue extends DatabaseQueue<UninstallRequest> {

    public UninstallQueue(Context context, JSONSerializable.Factory<UninstallRequest> uninstallRecordFactory) {
        super(context, "uninstall_queue", uninstallRecordFactory);
    }
}