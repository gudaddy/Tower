package com.baxterpad.towerrules;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Chen on 8/8/2015.
 */
// Don't hide action bar for settings. That's why this class doesn't extend ActivityHideSystemUI
public class ActivitySettings extends ActivityHideSystemUI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

            findPreference("pref_key_feedback").setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference p) {
                            // Display the fragment as the main content.
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(android.R.id.content, new FeedbackFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                            return false;
                        }
                    });

            findPreference("pref_key_about").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference p) {
                        // Display the fragment as the main content.
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(android.R.id.content, new AboutFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return false;
                    }
                });

//            findPreference("pref_key_edit_rules").setOnPreferenceClickListener(
//                new Preference.OnPreferenceClickListener() {
//                    @Override
//                    public boolean onPreferenceClick(Preference p) {
//                        Intent intent = new Intent(p.getContext(), ActivityEditRules.class);
//                        startActivity(intent);
//                        return false;
//                    }
//
//                });
        }
    }

    public static class AboutFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.preference_about, container, false);
        }
    }

    public static class FeedbackFragment extends Fragment implements View.OnClickListener {

        private static String feedback_message = "";
        private static String feedback_title_template = "";
        private static final int MAX_MESSAGE_LEN = 500;

        private Button submitButton;
        private Button cancelButton;
        private EditText editText;
        private TextView title;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.preference_feedback, container, false);

            submitButton = (Button) view.findViewById(R.id.feedback_submit_button);
            cancelButton = (Button) view.findViewById(R.id.feedback_cancel_button);
            title = (TextView) view.findViewById(R.id.pref_feedback_title);
            editText = (EditText) view.findViewById(R.id.pref_feedback_value);

            submitButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);

            feedback_title_template = getResources().getString(R.string.feedback_title);
            String text = String.format(feedback_title_template, MAX_MESSAGE_LEN);
            title.setText(text);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable arg0) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    enableSubmitIfReady();
                    updateCharactersRemaining();
                }
            });

            return view;
        }

        public void enableSubmitIfReady() {
            boolean isReady = editText.getText().toString().length() > 0;
            submitButton.setEnabled(isReady);
        }

        public void updateCharactersRemaining() {
            int charRemaining = MAX_MESSAGE_LEN - editText.getText().toString().length();
            String text = String.format(feedback_title_template, charRemaining);
            title.setText(text);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.feedback_submit_button:
                    if (editText != null && editText.getText() != null) {
                        feedback_message = editText.getText().toString();
                        sendEmail();
                        alert(v);
                    }

                    resetFeedback();
                    // Kills this fragment and returns to Settings
                    getActivity().onBackPressed();
                    break;
                case R.id.feedback_cancel_button:
                    resetFeedback();
                    // Kills this fragment and returns to Settings
                    getActivity().onBackPressed();
                    break;
                default:
                    break;
            }
        }

        private static void alert(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Thank you! :)");
            AlertDialog alert = builder.create();
            alert.show();
        }

        private static void sendEmail() {
            //TODO implement
        }

        private static void resetFeedback() {
            feedback_message = "";
        }
    }
}

