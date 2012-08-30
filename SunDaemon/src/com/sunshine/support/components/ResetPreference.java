package com.sunshine.support.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import com.sunshine.support.R;
import com.sunshine.support.activities.SystemConfigsActivity;

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
