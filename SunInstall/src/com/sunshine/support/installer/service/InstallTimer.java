package com.sunshine.support.installer.service;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;

public class InstallTimer extends CountDownTimer{

    private Context context;
    private PowerManager pm;

    public InstallTimer(Context context, long millisInFuture) {
        super(millisInFuture, 10000);
        this.context = context;
        this.pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Log.v(getClass().getName(), "Starting installation in " + millisUntilFinished / 1000);
    }

    @Override
    public void onFinish() {
        if(!pm.isScreenOn()){
            Intent i = new Intent(context, InstallerService.class);
            i.setAction(InstallerService.ACTION_INSTALL);
            context.startService(i);
        }
    }
}
