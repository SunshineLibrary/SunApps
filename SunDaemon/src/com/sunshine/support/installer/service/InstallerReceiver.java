package com.sunshine.support.installer.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class InstallerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent i = new Intent("sunshine.supportservice.installactual");
		i.setClass(context, InstallerService.class);
		context.startService(i);
	}

}
