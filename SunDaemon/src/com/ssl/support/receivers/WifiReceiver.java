package com.ssl.support.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.ssl.support.config.AccessToken;

import java.util.List;

public class WifiReceiver extends BroadcastReceiver {

    private static final String SUNSHINE_NETWORKS_SSID = "\"Sunshine Networks\"";
    private static final String SUNSHINE_NETWORKS_PASSWORD = "\"yangguang\"";

    private static final String TAG = "WifiReceiver";

    private static boolean resettingConnection;

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String action = intent.getAction();

        if (AccessToken.getAccountType(context).equals(AccessToken.ACCOUNT_TYPE_TEACHER)) {
            return;
        }

        if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (alreadyConnected(connectionInfo)) {
                    Log.d(TAG, "Already connected to: " + SUNSHINE_NETWORKS_SSID);
                } else {
                    Log.d(TAG, "Disconnecting current network.");
                    wifiManager.disconnect();
                    resettingConnection = true;
                }
            } else if (resettingConnection) {
                resettingConnection = false;
                setupNetwork(wifiManager);
            }
        } else if (action.equals("android.net.wifi.WIFI_STATE_CHANGED")) {
            if (wifiManager.isWifiEnabled()) {
                setupNetwork(wifiManager);
            }
        } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            } else {
                setupNetwork(wifiManager);
            }
        }
    }

    private void setupNetwork(WifiManager wifiManager) {
        removeExistingNetworks(wifiManager);
        addSunshineNetworks(wifiManager);
    }

    private void removeExistingNetworks(WifiManager wifiManager) {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        Log.d(TAG, "Number of Wifi configurations: " + configuredNetworks.size());
        for (WifiConfiguration config : configuredNetworks) {
            if(wifiManager.removeNetwork(config.networkId)) {
                Log.d(TAG, "Removed network: " + config.SSID);
            }
        }
        wifiManager.saveConfiguration();
        Log.d(TAG, "Number of Wifi configurations remaining: " + wifiManager.getConfiguredNetworks().size());
    }

    private void addSunshineNetworks(WifiManager wifiManager) {
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = SUNSHINE_NETWORKS_SSID;
        config.preSharedKey = SUNSHINE_NETWORKS_PASSWORD;
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        int networkId = wifiManager.addNetwork(config);

        if (networkId >= 0) {
            Log.d(TAG, "Network added: " + config.SSID);
            boolean success = wifiManager.enableNetwork(networkId, true);
            Log.d(TAG, "Enabled network: " + success);
            wifiManager.saveConfiguration();
        } else {
            Log.d(TAG, "Failed to add network: " + config.SSID);
        }
    }


    private boolean alreadyConnected(WifiInfo connectionInfo) {
        if (connectionInfo == null) {
            return false;
        }

        String wifiSSID = connectionInfo.getSSID();
        if (wifiSSID != null && SUNSHINE_NETWORKS_SSID.equals("\"" + wifiSSID + "\"")) {
            return true;
        }

        wifiSSID = connectionInfo.getBSSID();
        if (wifiSSID != null && SUNSHINE_NETWORKS_SSID.equals("\"" + wifiSSID + "\"")) {
            return true;
        }
        return false;
    }
}