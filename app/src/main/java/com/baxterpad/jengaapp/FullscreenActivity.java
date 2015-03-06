package com.baxterpad.jengaapp;

import com.baxterpad.jengaapp.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = false;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private static boolean GAME_IN_PROGRESS = false;

    private static final String[] rules = {
            "Rule #1",
            "Rule #2",
            "Rule #3",
            "Rule #4",
            "Rule #5",
            "Rule #6",
            "Rule #7",
            "Rule #8",
            "Rule #9",
            "Rule #10",
    };

    private static String[] current_rules;
    private static int rule_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);
        final Button startButton = (Button) findViewById(R.id.dummy_button);
        final TextView txtView = (TextView) findViewById(R.id.fullscreen_content);

        controlsView.setVisibility(View.INVISIBLE);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        /*
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
//                            controlsView.setVisibility(View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });
        */
        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (TOGGLE_ON_CLICK) {
//                    mSystemUiHider.toggle();
//                } else {
//                    //mSystemUiHider.show();
//                    mSystemUiHider.hide();
//                }

                if (GAME_IN_PROGRESS) {
                    //GAME_IN_PROGRESS = false;

                    if (rule_index >= current_rules.length) {
                        current_rules = shuffleRules();
                        rule_index = 0;
                    }
                    txtView.setText(current_rules[rule_index]);
                    rule_index++;

                } else {
                    GAME_IN_PROGRESS = true;
                    //TextView txtView = (TextView) findViewById(R.id.fullscreen_content);
                    if (rule_index >= current_rules.length) {
                        current_rules = shuffleRules();
                        rule_index = 0;
                    }
                    txtView.setText(current_rules[rule_index]);
                    rule_index++;

                    //TextView txtView = (TextView) findViewById(R.id.fullscreen_content);
                    //Button startButton = (Button) findViewById(R.id.dummy_button);
                    startButton.setText(R.string.button_restart);
                    //startButton.setVisibility(View.VISIBLE);
                    controlsView.setVisibility(View.VISIBLE);
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //@TODO game state: waiting for start
        //@TODO game state: once started, display 'restart' button, display rule
        //@TODO game event: on tap, display new rule
        //@TODO game event: on restart, go back to start
        //@TODO formatting - change font based on length and wrap strings and screen orientation
        //@TODO game: after it loops around...rerandomize it
        //@TODO game: make sure rules are limited to 256 characters?
        //@TODO game: give it a second to hide UI?
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


//        startButton.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v) {
//
//            }
//        });

        startButton.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                // --- find the text view --
                //TextView txtView = (TextView) findViewById(R.id.fullscreen_content);

                if (GAME_IN_PROGRESS) {
                    GAME_IN_PROGRESS = false;
                    txtView.setText(R.string.instructions);
                    //startButton.setText(R.string.button_start);
                    controlsView.setVisibility(View.INVISIBLE);
                    //startButton.setVisibility(View.GONE);
                    current_rules = shuffleRules();
                    rule_index = 0;
                } else {
                    GAME_IN_PROGRESS = true;
                    //txtView.setText("Started");
                    startButton.setText(R.string.button_restart);
                    controlsView.setVisibility(View.VISIBLE);
                    //startButton.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        current_rules = shuffleRules();
        rule_index = 0;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            //mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private String[] shuffleRules() {
        String[] temp_rules = new String[rules.length];
        for (int i = 0; i < rules.length; i++) {
            temp_rules[i] = rules[i];
        }

        int newI;
        String temp_rule;
        Random randIndex = new Random();

        for (int i = 0; i < rules.length; i++) {

            // pick a random index between 0 and cardsInDeck - 1
            newI = randIndex.nextInt(rules.length);

            // swap cards[i] and cards[newI]
            temp_rule = temp_rules[i];
            temp_rules[i] = temp_rules[newI];
            temp_rules[newI] = temp_rule;
        }

        return temp_rules;
    }
}
