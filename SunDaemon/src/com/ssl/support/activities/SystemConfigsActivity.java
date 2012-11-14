package com.ssl.support.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.Preference;
import com.ssl.support.daemon.R;
import android.content.pm.PackageInfo;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class SystemConfigsActivity extends PreferenceActivity{

    private Preference mVersionNamePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pinfo.versionName;
        mVersionNamePref = findPreference("version_name");
        mVersionNamePref.setSummary(versionName);
    }
}
