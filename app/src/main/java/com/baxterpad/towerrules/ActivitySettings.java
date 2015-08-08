package com.baxterpad.towerrules;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by Chen on 8/8/2015.
 */
// Don't hide action bar for settings. That's why this class doesn't extend ActivityHideSystemUI
public class ActivitySettings extends ActivityHideSystemUI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_settings);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

//            String selected = sharedPrefs.getString(
//                    getString(R.string.list_preference_array),
//                    "default string"
//            );
//            int index = mylistpreference.findIndexOfValue("about");
//            String entry = mylistpreference.getEntries()[index];
//            Preference preference = getPreferenceScreen().getPreference("");
//            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
//                @Override
//                public boolean onPreferenceClick(Preference p){
//                    //do something
//                    return false;
//                }
//            });
        }
    }
}

