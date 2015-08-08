package com.baxterpad.towerrules;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chen on 8/8/2015.
 */
public class ActivityHelp extends ActivityHideSystemUI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        // Display the fragment as the main content.
//        getFragmentManager().beginTransaction()
//                .replace(android.R.id.content, new HelpFragment())
//                .commit();
    }

//    public static class HelpFragment extends Fragment {
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.activity_help, container, false);
//            return view;
//        }
//    }
}


