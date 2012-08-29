package com.sunshine.support.installer.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

public class InstallTimer {

    private Context context;
    private PowerManager pm;
    private Handler handler;
    private Runnable runnable;
    private int delayInMillis;

    public InstallTimer(Context context, int delayInMillis) {
        this.context = context;
        this.pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        this.handler = new Handler();
        this.runnable = new MRunnable();
        this.delayInMillis = delayInMillis;
    }

    public void start() {
        handler.postDelayed(runnable, delayInMillis);
    }

    public void reset() {
        handler.removeCallbacks(runnable);
    }

    private class MRunnable implements Runnable {
        @Override
        public void run() {
            Log.v(getClass().getName(), "Timer up. Starting Installation");
            if(!pm.isScreenOn()){
                Intent i = new Intent(context, InstallerService.class);
                i.setAction(InstallerService.ACTION_INSTALL);
                context.startService(i);
            } else {
                Log.v(getClass().getName(), "Screen on. Timer stopped.");
            }
        }
    }
}
