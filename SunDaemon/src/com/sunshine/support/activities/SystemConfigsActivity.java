package com.sunshine.support.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.sunshine.support.R;

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
