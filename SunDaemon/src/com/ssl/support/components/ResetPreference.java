package com.ssl.support.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import com.ssl.support.daemon.R;
import com.ssl.support.activities.SystemConfigsActivity;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class ResetPreference extends DialogPreference{

        public ResetPreference(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onDialogClosed(boolean positiveResult) {
            if (positiveResult) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
                pref.edit().clear().commit();
                SystemConfigsActivity activity = ((SystemConfigsActivity) getContext());
                activity.getPreferenceScreen().removeAll();
                activity.addPreferencesFromResource(R.xml.preferences);
            }
        }
}
