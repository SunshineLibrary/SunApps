package com.ssl.support.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.PowerManager;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class LockManager {

    private static String TAG = "LockManager";

    private static LockManager singleton;

    private WifiManager.WifiLock wifiLock;
    private PowerManager.WakeLock wakeLock;
    private Set<Token> wifiTokens;
    private Set<Token> wakeTokens;

    public LockManager(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wakeTokens = new HashSet<Token>();

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiLock = wifiManager.createWifiLock(TAG);
        wifiTokens = new HashSet<Token>();
    }

    public static LockManager getInstance(Context context) {
        if (singleton == null) {
            singleton = new LockManager(context);
        }
        return singleton;
    }

    public Token acquireWakeLock(Token token) {
        if (token == null) {
            token = new Token();
        } else if (wakeTokens.contains(token)) {
            return token;
        }

        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }

        wakeTokens.add(token);
        return token;
    }

    public Token acquireWifiLock(Token token) {
        // must ensure CPU is awake.
        token = acquireWakeLock(token);

        if (wifiTokens.contains(token)) {
            return token;
        }

        if (!wifiLock.isHeld()) {
            wifiLock.acquire();
        }

        wifiTokens.add(token);
        return token;
    }

    public void releaseLock(Token token) {
        if (token != null) {
            releaseWifiLock(token);
            releaseWakeLock(token);
        }
    }

    private void releaseWifiLock(Token token) {
        if (wifiTokens.contains(token)) {
            wifiTokens.remove(token);
            if (wifiTokens.isEmpty()) {
                wifiLock.release();
            }
        }
    }

    private void releaseWakeLock(Token token) {
        if (wakeTokens.contains(token)) {
            wakeTokens.remove(token);
            if (wakeTokens.isEmpty()) {
                wakeLock.release();
            }
        }
    }

    public static class Token {
        private static int tokenIdCounter = 0;
        private int mTokenId = tokenIdCounter ++;

        @Override
        public int hashCode() {
            return mTokenId;
        }
    }
}
