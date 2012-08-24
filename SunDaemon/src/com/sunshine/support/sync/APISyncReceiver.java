package com.sunshine.support.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.sunshine.support.update.UpdateService;

public class APISyncReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			context.startService(new Intent(context, APISyncService.class));
            context.startService(new Intent(context, UpdateService.class));
		} else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			boolean isDisconnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
			if (!isDisconnected) {
				context.startService(new Intent(context, APISyncService.class));
			}
		}
	}
}
