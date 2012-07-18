package com.sunshine.support;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootstrapReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.equals(Intent.ACTION_BOOT_COMPLETED)){
			context.startService(intent);
		}
	}

}
