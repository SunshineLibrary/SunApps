package com.sunshine.support.installer.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;

public class InstallerReceiver extends BroadcastReceiver {

	@Override
	public synchronized void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            Log.v(getClass().getName(), "Try stopping install timer.");
            Intent i = new Intent(context, InstallerService.class);
            i.setAction(InstallerService.ACTION_STOP_TIMER);
            context.startService(i);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) ||
                intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.v(getClass().getName(), "Try starting install timer.");
            Intent i = new Intent(context, InstallerService.class);
            i.setAction(InstallerService.ACTION_START_TIMER);
            context.startService(i);
        }
	}
}