package com.ssl.support.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class ConnectionUtils {

    private static ConnectivityManager cm;
    private static String TAG = "ConnectionUtils";

    public static boolean isConnected(Context context) {
        initConnectivityManager(context);
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            return cm.getActiveNetworkInfo().isConnected();
        } else {
            Log.v(TAG, "Device is not connected...");
            return false;
        }
    }

    private static void initConnectivityManager(Context context) {
        if (cm == null) {
            cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
    }
}
