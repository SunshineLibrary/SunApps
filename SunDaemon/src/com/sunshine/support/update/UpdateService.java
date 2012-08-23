package com.sunshine.support.update;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdateService extends Service {

    UpdateTask task;
    boolean updateInProgress;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        task = new UpdateTask(this) {
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                stopSelf();
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!updateInProgress) {
            updateInProgress = true;
            task.execute();
        }
        return START_NOT_STICKY;
    }
}
