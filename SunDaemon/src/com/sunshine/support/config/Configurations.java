package com.sunshine.support.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.sunshine.support.R;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class Configurations {

    public static String API_SERVER_ADDRESS = "api_server_address";
    public static String UPLOAD_SERVER_ADDRESS = "upload_server_address";
    public static String LOG_SERVER_ADDRESS = "log_server_address";

    private SharedPreferences preference;
    private Context context;

    public Configurations(Context context) {
        this.context = context;
        this.preference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getString(String key) {
        return preference.getString(key, getDefaultString(key));
    }

    public synchronized void putString(String key, String value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getDefaultString(String key) {
        if (key.equals(API_SERVER_ADDRESS))
            return context.getString(R.string.default_api_server_address);
        if (key.equals(UPLOAD_SERVER_ADDRESS))
            return context.getString(R.string.default_upload_server_address);
        if (key.equals(LOG_SERVER_ADDRESS))
            return context.getString(R.string.default_log_server_address);
        return "";
    }
}
