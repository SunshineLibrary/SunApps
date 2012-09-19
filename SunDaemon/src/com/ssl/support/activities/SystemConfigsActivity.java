package com.ssl.support.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.ssl.support.daemon.R;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class SystemConfigsActivity extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
