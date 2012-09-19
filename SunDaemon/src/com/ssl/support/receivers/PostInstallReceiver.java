package com.ssl.support.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.data.helpers.PackageHelper;
import com.ssl.support.data.models.Package;

public class PostInstallReceiver extends BroadcastReceiver {

    public static final String ACTION_PACKAGE_INSTALLED = "com.ssl.support.action.packageInstalled";
    public static final String ACTION_PACKAGE_INSTALL_FAILED = "com.ssl.support.action.packageInstallFailed";
    public static final String ACTION_INSTALL_STOPPED = "com.ssl.support.action.installStopped";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_PACKAGE_INSTALLED)){
            Log.v(getClass().getName(), "Received successful install: " + intent.getData());
            Package pkg = PackageHelper.getPackageForFile(context, intent.getData());
            PackageHelper.setInstallStatus(context, pkg.id, MetadataContract.Packages.INSTALL_STATUS_INSTALLED);
		} else if (intent.getAction().equals(ACTION_PACKAGE_INSTALL_FAILED)) {
            Log.v(getClass().getName(), "Received failed install: " + intent.getData());
            Package pkg = PackageHelper.getPackageForFile(context, intent.getData());
            PackageHelper.setInstallStatus(context, pkg.id, MetadataContract.Packages.INSTALL_STATUS_FAILED);
		} else if (intent.getAction().equals(ACTION_INSTALL_STOPPED)) {
            Log.v(getClass().getName(), "Received install stopped.");
            PackageHelper.setPendingToFailed(context);
        } else {
            Log.v(getClass().getName(), "Received broadcast: " + intent);
        }
	}
}
