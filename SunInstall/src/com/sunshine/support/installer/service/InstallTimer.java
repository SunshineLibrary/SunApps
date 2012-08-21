package com.sunshine.support.installer.service;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;

public class InstallTimer implements Runnable {

    private Context context;
    private PowerManager pm;

    public InstallTimer(Context context) {
        this.context = context;
        this.pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

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
