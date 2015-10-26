package net.dkisselev.zentime;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Activity to show the preferences using a fragment.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}