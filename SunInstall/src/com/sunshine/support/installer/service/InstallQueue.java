package com.sunshine.support.installer.service;

import android.content.Context;
import com.sunshine.support.installer.utils.DatabaseQueue;
import com.sunshine.support.installer.utils.JSONSerializable;

public class InstallQueue extends DatabaseQueue<InstallRequest> {

    public InstallQueue(Context context, JSONSerializable.Factory<InstallRequest> installRecordFactory) {
        super(context, "install_queue", installRecordFactory);
    }
}
