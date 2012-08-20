package com.sunshine.support.update;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        new UpdateTask(this).execute();
        return START_NOT_STICKY;
    }
}
