package com.khoa.ptittools.setting;

import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.khoa.ptittools.R;
import com.khoa.ptittools.base.helper.SettingHelper;
import com.khoa.ptittools.base.helper.SharedPreferencesHelper;

public class SettingsFragment extends PreferenceFragmentCompat {

    private Preference periodicUpdatePref;
    private Preference autoUpdatePref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_setting, rootKey);

        bindPreference();
        bindPreferenceListener();
    }

    private void bindPreference() {
        autoUpdatePref = findPreference(getString(R.string.auto_update));
        periodicUpdatePref = findPreference(getString(R.string.periodic_update));
    }

    private void bindPreferenceListener() {
        autoUpdatePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean autoUpdate = (boolean) newValue;
                if (!autoUpdate) {
                    SettingHelper.getInstance().cancelAutoUpdate();
                } else {
                    SettingHelper.getInstance().runAutoUpdate();
                }
                return true;
            }
        });

        periodicUpdatePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferencesHelper.getInstance().setPeriodicValue(newValue.toString());
                SettingHelper.getInstance().runAutoUpdate();
                return true;
            }
        });
    }

}