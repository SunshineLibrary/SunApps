package com.ssl.support.install.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InstallReceiver extends BroadcastReceiver {

	@Override
	public synchronized void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            Log.v(getClass().getName(), "Try stopping install timer.");
            Intent i = new Intent(context, InstallService.class);
            i.setAction(InstallService.ACTION_STOP_TIMER);
            context.startService(i);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) ||
                intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.v(getClass().getName(), "Try starting install timer.");
            Intent i = new Intent(context, InstallService.class);
            i.setAction(InstallService.ACTION_START_TIMER);
            context.startService(i);
        } else {
            Log.v(getClass().getName(), "Received Broadcast: " + intent);
        }
	}
}