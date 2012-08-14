package com.sunshine.support.installer.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InstallerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent i = new Intent("com.sunshine.support.action.install");
		i.setClass(context, InstallerService.class);
		context.startService(i);
	}
}