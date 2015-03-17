package com.baxterpad.jengaapp;

import com.baxterpad.jengaapp.util.SystemUiHider;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// @TODO Make sure when in landscape that flipping it upside down will reorient the screen.


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

    /**
     * Game variables
     */
    private static boolean GAME_IN_PROGRESS = false;
    private static String[] current_rules;
    private static int rule_index = 0;

    /**
     * A shaker object for detecting when the device has been shaken (not stirred).
     */
    private ShakeListener mShaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Rules.initializeRules();
        } catch (Exception e) {
            e.printStackTrace();
        }

        current_rules = Rules.shuffleRules();
        rule_index = 0;

        setContentView(R.layout.activity_fullscreen);

        View decorView = getWindow().getDecorView();

        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // UI objects for modifying the UI based on game state
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);
        final Button startButton = (Button) findViewById(R.id.dummy_button);
        final TextView txtView = (TextView) findViewById(R.id.fullscreen_content);

        controlsView.setVisibility(View.INVISIBLE);

        // Action based on shake
        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
            public void onShake() {
                gameLogic(controlsView, startButton, txtView);
            }
        });

        // Action based on click
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameLogic(controlsView, startButton, txtView);
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
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
                    current_rules = Rules.shuffleRules();
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

    @Override
    public void onResume() {
        mShaker.resume();
        super.onResume();
    }
    @Override
    public void onPause() {
        mShaker.pause();
        super.onPause();
    }

    /**
     * Game logic code. This will also update the UI based on the state of the game.
     *
     * @TODO better design this and separate game logic from UI code
     *
     * game state: waiting for start (hide 'restart' button)
     * game state: once started, display 'restart' button, display rule
     * game event: on tap, display new rule
     * game event: on restart, go back to start
     * @param controlsView
     * @param startButton
     * @param txtView
     */
    private static void gameLogic(
        View controlsView,
        Button startButton,
        TextView txtView) {

        if (GAME_IN_PROGRESS) {
            //GAME_IN_PROGRESS = false;

            if (rule_index >= current_rules.length) {
                current_rules = Rules.shuffleRules();
                rule_index = 0;
            }
            txtView.setText(current_rules[rule_index]);
            //@TODO formatting - change font size based on length and screen orientation
            //@TODO make sure rules are limited to 256 characters?
            //android:textSize="50sp"
            // portrait 14 * 11 = 154
            // landscape 6 * 21 = 126
            rule_index++;

        } else {
            GAME_IN_PROGRESS = true;
            if (rule_index >= current_rules.length) {
                current_rules = Rules.shuffleRules();
                rule_index = 0;
            }
            txtView.setText(current_rules[rule_index]);
            rule_index++;

            startButton.setText(R.string.button_restart);
            controlsView.setVisibility(View.VISIBLE);
        }
    }

}
