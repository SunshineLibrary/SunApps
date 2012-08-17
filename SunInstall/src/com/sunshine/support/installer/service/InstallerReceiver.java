package com.sunshine.support.installer.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.PowerManager;

public class InstallerReceiver extends BroadcastReceiver {

    private InstallTimer timer;

    private static final int INSTALL_DELAY = 60000;

	@Override
	public synchronized void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (timer == null) {
                timer = new InstallTimer(context, INSTALL_DELAY);
                timer.start();
            }
        }
	}
}