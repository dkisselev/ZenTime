package net.dkisselev.zentime;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import java.io.File;

public class SettingsFragment extends PreferenceFragment {
    PreferenceScreen preferenceScreen;
    Preference aboutPref;
    Preference timeOptionsPref;
    Preference customTimePref;
    Preference softRebookPref;

    SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the preferences file world-readable so that xposed has access
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);

        // Cache context references
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferenceScreen = getPreferenceScreen();
        aboutPref = findPreference("pref_about");
        timeOptionsPref = findPreference("pref_time_options");
        customTimePref = findPreference("pref_custom_times");
        softRebookPref = findPreference("pref_soft_reboot");

        // Apply custom attributes and listeners to settings
        aboutPref.setTitle(getString(R.string.pref_about_title, BuildConfig.VERSION_NAME));
        timeOptionsPref.setOnPreferenceChangeListener(timeOptionsChangeListener);

        // Disable the custom option if the time option wasn't already set to custom
        String currentTimeOption = sharedPrefs.getString(timeOptionsPref.getKey(), "");
        if (!currentTimeOption.equals("custom")) {
            customTimePref.setEnabled(false);
        }
    }

    /**
     * When the option setting is changed, apply one of the builtin values and disable the input
     * or enable the input for the user to customizes
     */
    private final Preference.OnPreferenceChangeListener timeOptionsChangeListener = new Preference.OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch ((String)newValue) {
                case "default":
                    customTimePref.setEnabled(false);
                    break;
                case "more":
                    customTimePref.setEnabled(false);
                    break;
                case "custom":
                    customTimePref.setEnabled(true);
                    break;
            }

            return true;
        }
    };


    @Override
    public void onPause() {
        super.onPause();

        // Set preferences file permissions to be world readable
        File sharedPrefsDir = new File(getActivity().getApplicationInfo().dataDir, "shared_prefs");
        File sharedPrefsFile = new File(sharedPrefsDir, getPreferenceManager().getSharedPreferencesName() + ".xml");
        if (sharedPrefsFile.exists()) {
            sharedPrefsFile.setReadable(true, false);
        }
    }
}
